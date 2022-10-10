package ru.yandex.practicum.explore_with_me.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.explore_with_me.event.client.EventClient;
import ru.yandex.practicum.explore_with_me.event.model.EndpointHitDto;
import ru.yandex.practicum.explore_with_me.event.model.Event.Event;
import ru.yandex.practicum.explore_with_me.event.model.Location;
import ru.yandex.practicum.explore_with_me.event.model.SortEnum;
import ru.yandex.practicum.explore_with_me.event.storage.EventRepository;
import ru.yandex.practicum.explore_with_me.event.storage.LocationRepository;
import ru.yandex.practicum.explore_with_me.exception.exceptions.NotFoundException;
import ru.yandex.practicum.explore_with_me.exception.exceptions.PrivilegeException;
import ru.yandex.practicum.explore_with_me.exception.exceptions.StateOrStatusException;
import ru.yandex.practicum.explore_with_me.exception.exceptions.ValidationException;
import ru.yandex.practicum.explore_with_me.user.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static ru.yandex.practicum.explore_with_me.event.model.State.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final LocationRepository locationRepository;
    private final EventClient eventClient;
    private final UserService userService;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public List<Event> getAllPublic(String text, Long[] categories,
                                    Boolean paid, String rangeStart,
                                    String rangeEnd, boolean onlyAvailable,
                                    SortEnum sortEnum, int from, int size, HttpServletRequest request) {

        //информацию о том, что по этому эндпоинту был осуществлен и обработан запрос,
        // нужно сохранить в сервисе статистики
        ResponseEntity<Object> responseEntity = eventClient.sendToStatistics(
                new EndpointHitDto(
                        "ExploreWithMe",
                        request.getRequestURI(),
                        request.getRemoteAddr(),
                        LocalDateTime.now().format(formatter))
        );

        log.debug("Status code {}", responseEntity.getStatusCode());

        List<Event> list;
        LocalDateTime start;
        LocalDateTime end;
        Sort srt = null;

        //если в запросе не указан диапазон дат [rangeStart-rangeEnd],
        // то нужно выгружать события, которые произойдут позже текущей даты и времени
        if (rangeEnd == null || rangeStart == null) {
            start = LocalDateTime.now();
            end = LocalDateTime.MAX;
        } else {
            start = LocalDateTime.parse(rangeStart, formatter);
            end = LocalDateTime.parse(rangeEnd, formatter);
        }

        switch (sortEnum) {
            case EVENT_DATE:
                srt = Sort.by(Sort.Direction.DESC, "dateOfEvent");
                break;
            case VIEWS:
                srt = Sort.by(Sort.Direction.DESC, "views");
                break;
        }
        Pageable pageRequest = PageRequest.of(from / size, size, srt);

        //в выдаче должны быть только опубликованные события State "PUBLISHED"
        list = eventRepository.findAllByDateOfEventIsAfterAndDateOfEventIsBeforeAndState(
                start, end, PUBLISHED, pageRequest).getContent();

        if (onlyAvailable) {
            list = list.stream().filter(
                            event -> event.getConfirmedRequests() <= event.getParticipantLimit())
                    .collect(Collectors.toList());
        }

        if (paid != null) {
            list = list.stream().filter(Event::getPaid)
                    .collect(Collectors.toList());
        }

        if (categories.length != 0) {
            List<Long> cat = Arrays.asList(categories);
            list = list.stream().filter(
                            event -> cat.contains(event.getCategory().getId()))
                    .collect(Collectors.toList());
        }
        //текстовый поиск (по аннотации и подробному описанию) должен быть без учета регистра букв
        if (text != null) {
            list = list.stream().filter
                            (event -> event.getDescription().toLowerCase().
                                    contains(text.toLowerCase())
                                    || event.getAnnotation().toLowerCase().
                                    contains(text.toLowerCase())).
                    collect(Collectors.toList());
        }
        log.debug("list of events returned: {}", list);
        return list;
    }

    @Override
    public Event getByEventIdPublic(long id, HttpServletRequest request) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Event", id));

        if (!event.getState().equals(PUBLISHED)) {
            StateOrStatusException e = new StateOrStatusException("Event is not PUBLISHED");
            log.error(e.getMessage());
            throw e;
        }

        //информацию о том, что по этому эндпоинту был осуществлен и обработан запрос,
        // нужно сохранить в сервисе статистики
        ResponseEntity<Object> responseEntity = eventClient.sendToStatistics(
                new EndpointHitDto(
                        "ExploreWithMe",
                        request.getRequestURI(),
                        request.getRemoteAddr(),
                        LocalDateTime.now().format(formatter))
        );

        log.debug("Status code {}", responseEntity.getStatusCode());
        if (responseEntity.getStatusCode().equals(HttpStatus.ACCEPTED)) {
            event.setViews(event.getViews() + 1);
        }

        eventRepository.save(event);
        log.debug("Event returned {}", event);
        return event;
    }

    @Override
    public List<Event> getAllByUser(long userId, int from, int size) {

        //валидация пользователя
        userService.getById(userId);

        List<Event> list;
        Pageable pageRequest = PageRequest.of(from / size, size);

        //в выдаче должны быть только опубликованные события State "PUBLISHED"
        list = eventRepository.findAllByInitiatorId(
                userId, pageRequest).getContent();

        log.debug("list of events returned: {}", list);
        return list;
    }

    @Override
    public Event getByUserIdAndEventId(long userId, long eventId) {

        //валидация пользователя
        userService.getById(userId);

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event", eventId));

        if (userId != event.getInitiator().getId()) {
            PrivilegeException e = new PrivilegeException("Событие не принадлежит пользвателю");
            log.error(e.getMessage());
            throw e;
        }

        log.debug("Event returned {}", event);
        return event;
    }


    @Override
    public Event create(Event event) {
        Event newEvent = eventRepository.save(event);
        log.debug("Event created/updated {}", newEvent);
        return newEvent;
    }

    @Override
    public Location createLoc(Location location) {
        Location newLocation = locationRepository.save(location);
        log.debug("Location created/updated {}", newLocation);
        return newLocation;
    }

    @Override
    public Event cancelEvent(long userId, long eventId) {

        Event event = getByUserIdAndEventId(userId, eventId);

        if (!event.getState().equals(PENDING)) {
            StateOrStatusException e = new StateOrStatusException
                    ("Отменить можно только событие в состоянии ожидания модерации.");
            log.error(e.getMessage());
            throw e;
        }

        event.setState(CANCELED);
        Event newEvent = eventRepository.save(event);
        log.debug("Event cancelled {}", newEvent);
        return newEvent;

    }

    @Override
    public List<Event> getAllAdmin(Long[] users, String[] states,
                                   Long[] categories, String rangeStart,
                                   String rangeEnd, int from, int size) {

        List<Event> list;
        LocalDateTime start;
        LocalDateTime end;

        //если в запросе не указан диапазон дат [rangeStart-rangeEnd],
        // то нужно выгружать события, которые произойдут позже текущей даты и времени
        if (rangeEnd == null || rangeStart == null) {
            start = LocalDateTime.now();
            end = LocalDateTime.MAX;
        } else {
            start = LocalDateTime.parse(rangeStart, formatter);
            end = LocalDateTime.parse(rangeEnd, formatter);
        }

        Pageable pageRequest = PageRequest.of(from / size, size);

        list = eventRepository.findAllByDateOfEventIsAfterAndDateOfEventIsBefore(
                start, end, pageRequest).getContent();

        if (users.length != 0) {
            List<Long> us = Arrays.asList(users);
            list = list.stream().filter(
                            event -> us.contains(event.getInitiator().getId()))
                    .collect(Collectors.toList());
        }

        if (states.length != 0) {
            List<String> st = Arrays.asList(states);
            list = list.stream().filter(
                            event -> st.contains(event.getState().name()))
                    .collect(Collectors.toList());
        }

        if (categories.length != 0) {
            List<Long> cat = Arrays.asList(categories);
            list = list.stream().filter(
                            event -> cat.contains(event.getCategory().getId()))
                    .collect(Collectors.toList());
        }

        log.debug("list of events returned: {}", list);
        return list;
    }

    @Override
    public Event getByEventId(long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Event", id));
    }

    @Override
    public Event publish(long eventId) {

        Event event = getByEventId(eventId);

        if (!event.getState().equals(PENDING)) {
            StateOrStatusException e = new StateOrStatusException
                    ("событие должно быть в состоянии ожидания публикации");
            log.error(e.getMessage());
            throw e;
        }

        //дата начала события должна быть не ранее чем за час от даты публикации.
        if (LocalDateTime.now().plus(1, ChronoUnit.HOURS).isAfter(event.getDateOfEvent())) {
            ValidationException e = new ValidationException("Попробуйте указать более позднее время и дату");
            log.error(e.getMessage());
            throw e;
        }

        event.setState(PUBLISHED);
        event.setPublishedOn(LocalDateTime.now());
        Event newEvent = eventRepository.save(event);
        log.debug("Event published {}", newEvent);
        return newEvent;

    }

    @Override
    public Event reject(long eventId) {

        Event event = getByEventId(eventId);

        if (event.getState().equals(PUBLISHED)) {
            StateOrStatusException e = new StateOrStatusException
                    ("событие не должно быть опубликовано.");
            log.error(e.getMessage());
            throw e;
        }

        event.setState(CANCELED);
        Event newEvent = eventRepository.save(event);
        log.debug("Event rejected {}", newEvent);
        return newEvent;

    }
}

