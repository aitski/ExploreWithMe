package ru.yandex.practicum.ExploreWithMe.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.ExploreWithMe.event.LocationRepository;
import ru.yandex.practicum.ExploreWithMe.event.client.EventClient;
import ru.yandex.practicum.ExploreWithMe.event.model.EndpointHitDto;
import ru.yandex.practicum.ExploreWithMe.event.model.Location;
import ru.yandex.practicum.ExploreWithMe.event.model.SortEnum;
import ru.yandex.practicum.ExploreWithMe.event.EventRepository;
import ru.yandex.practicum.ExploreWithMe.event.model.Event.Event;
import ru.yandex.practicum.ExploreWithMe.exception.exceptions.NotFoundException;
import ru.yandex.practicum.ExploreWithMe.exception.exceptions.ValidationException;
import ru.yandex.practicum.ExploreWithMe.user.service.UserService;


import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

import static ru.yandex.practicum.ExploreWithMe.event.model.State.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final LocationRepository locationRepository;
    private final EventClient eventClient;
    private final UserService userService;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public List<Event> getAllPublic(Optional<String> text, Optional<Long[]> categories,
                              Optional<Boolean> paid, Optional<String> rangeStart,
                              Optional<String> rangeEnd, boolean onlyAvailable,
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
        if (rangeEnd.isEmpty() || rangeStart.isEmpty()) {
            start = LocalDateTime.now();
            end = LocalDateTime.MAX;
        } else {
            start = LocalDateTime.parse(rangeStart.get(),formatter);
            end = LocalDateTime.parse(rangeEnd.get(),formatter);
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

        if (paid.isPresent()) {
            list = list.stream().filter(Event::getPaid)
                    .collect(Collectors.toList());
        }

        if (categories.isPresent()) {
            List<Long> cat = Arrays.asList(categories.get());
            list = list.stream().filter(
                            event -> cat.contains(event.getCategory().getId()))
                    .collect(Collectors.toList());
        }
        //текстовый поиск (по аннотации и подробному описанию) должен быть без учета регистра букв
        if (text.isPresent()) {
            list = list.stream().filter
                            (event -> event.getDescription().toLowerCase().
                                    contains(text.get().toLowerCase())
                                    || event.getAnnotation().toLowerCase().
                                    contains(text.get().toLowerCase())).
                    collect(Collectors.toList());
        }
        log.debug("list of events returned: {}", list);
        return list;
    }

    @Override
    public Event getByEventIdPublic(long id, HttpServletRequest request) {
        Event event = eventRepository.findById(id).orElseThrow
                (() -> {
                    log.error("Event with id {} not found", id);
                    return new NotFoundException();
                });

        if(!event.getState().equals(PUBLISHED)){
            ValidationException e = new ValidationException("Event is not PUBLISHED");
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
        list = eventRepository.findAllByInitiator_Id(
                userId, pageRequest).getContent();

        log.debug("list of events returned: {}", list);
        return list;
    }

    @Override
    public Event getByUserIdAndEventId(long userId, long eventId) {

        //валидация пользователя
        userService.getById(userId);

        Event event = eventRepository.findById(eventId).
                orElseThrow(() -> {
                    log.error("Event with id {} not found", eventId);
                    return new NotFoundException();
                });

        if (userId != event.getInitiator().getId()) {
            ValidationException e = new ValidationException("Событие не принадлежит пользвателю");
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

       Event event = getByUserIdAndEventId(userId,eventId);

        if(!event.getState().equals(PENDING)){
            ValidationException e = new ValidationException
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
    public List<Event> getAllAdmin(Optional<Long[]> users, Optional<String[]> states,
                                   Optional<Long[]> categories, Optional<String> rangeStart,
                                   Optional<String> rangeEnd, int from, int size) {

        List<Event> list;
        LocalDateTime start;
        LocalDateTime end;

        //если в запросе не указан диапазон дат [rangeStart-rangeEnd],
        // то нужно выгружать события, которые произойдут позже текущей даты и времени
        if (rangeEnd.isEmpty() || rangeStart.isEmpty()) {
            start = LocalDateTime.now();
            end = LocalDateTime.MAX;
        } else {
            start = LocalDateTime.parse(rangeStart.get(),formatter);
            end = LocalDateTime.parse(rangeEnd.get(),formatter);
        }

        Pageable pageRequest = PageRequest.of(from / size, size);

        list = eventRepository.findAllByDateOfEventIsAfterAndDateOfEventIsBefore(
                start, end, pageRequest).getContent();

        if (users.isPresent()) {
            List<Long> us = Arrays.asList(users.get());
            list = list.stream().filter(
                            event -> us.contains(event.getInitiator().getId()))
                    .collect(Collectors.toList());
        }

        if (states.isPresent()) {
            List<String> st = Arrays.asList(states.get());
            list = list.stream().filter(
                            event -> st.contains(event.getState().name()))
                    .collect(Collectors.toList());
        }

        if (categories.isPresent()) {
            List<Long> cat = Arrays.asList(categories.get());
            list = list.stream().filter(
                            event -> cat.contains(event.getCategory().getId()))
                    .collect(Collectors.toList());
        }

        log.debug("list of events returned: {}", list);
        return list;
    }

    @Override
    public Event getByEventId(long id) {
        return eventRepository.findById(id).orElseThrow
                (() -> {
                    log.error("Event with id {} not found", id);
                    return new NotFoundException();
                });
    }

    @Override
    public Event publish(long eventId) {

        Event event = getByEventId(eventId);

        if(!event.getState().equals(PENDING)){
            ValidationException e = new ValidationException
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
    public Event reject (long eventId) {

        Event event = getByEventId(eventId);

        if(event.getState().equals(PUBLISHED)){
            ValidationException e = new ValidationException
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

