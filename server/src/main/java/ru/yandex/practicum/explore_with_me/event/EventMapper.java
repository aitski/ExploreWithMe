package ru.yandex.practicum.explore_with_me.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.explore_with_me.category.model.Category;
import ru.yandex.practicum.explore_with_me.category.service.CategoryService;
import ru.yandex.practicum.explore_with_me.event.model.Event.*;
import ru.yandex.practicum.explore_with_me.event.model.Location;
import ru.yandex.practicum.explore_with_me.event.service.EventService;
import ru.yandex.practicum.explore_with_me.exception.exceptions.ValidationException;
import ru.yandex.practicum.explore_with_me.user.model.User;
import ru.yandex.practicum.explore_with_me.user.service.UserService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import static ru.yandex.practicum.explore_with_me.event.model.State.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventMapper {

    private final UserService userService;
    private final CategoryService categoryService;
    private final EventService eventService;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public EventShortDto convertToShortDto(Event event) {

        Category cat = event.getCategory();
        EventShortDto.Category category = new EventShortDto.Category(cat.getId(), cat.getName());

        User init = event.getInitiator();
        EventShortDto.User initiator = new EventShortDto.User(init.getId(), init.getName());

        return new EventShortDto(
                event.getAnnotation(),
                category,
                event.getConfirmedRequests(),
                event.getDateOfEvent().format(formatter),
                event.getId(),
                initiator,
                event.getPaid(),
                event.getTitle(),
                event.getViews());
    }

    public EventFullDto convertToFullDto(Event event) {

        Category cat = event.getCategory();
        EventFullDto.Category category = new EventFullDto.Category(cat.getId(), cat.getName());

        User init = event.getInitiator();
        EventFullDto.User initiator = new EventFullDto.User(init.getId(), init.getName());

        Location loc = event.getLocation();
        EventFullDto.Location location = new EventFullDto.Location(loc.getLat(), loc.getLon());

        StringBuilder pub = new StringBuilder();
        if (event.getPublishedOn() != null) {
            pub.append(event.getPublishedOn().format(formatter));
        }
        String publishedOn = pub.toString();

        return new EventFullDto(
                event.getAnnotation(),
                category,
                event.getConfirmedRequests(),
                event.getCreatedOn().format(formatter),
                event.getDescription(),
                event.getDateOfEvent().format(formatter),
                event.getId(),
                initiator,
                location,
                event.getPaid(),
                event.getParticipantLimit(),
                publishedOn,
                event.getRequestModeration(),
                event.getState().name(),
                event.getTitle(),
                event.getViews());
    }

    public Event convertFromNewEventDto(long userId, NewEventDto eventDto) {
        Event event = new Event();
        Location loc = new Location();

        User initiator = userService.getById(userId);
        Category category = categoryService.getById(eventDto.getCategory());
        LocalDateTime eventDateDto = LocalDateTime.parse(eventDto.getEventDate(), formatter);
        loc.setLat(eventDto.getLocation().getLat());
        loc.setLon(eventDto.getLocation().getLon());

        // дата и время на которые намечено событие не может быть раньше,
        // чем через два часа от текущего момента
        if (LocalDateTime.now().plus(2, ChronoUnit.HOURS).isAfter(eventDateDto)) {
            ValidationException e = new ValidationException("Попробуйте указать более позднее время и дату");
            log.error(e.getMessage());
            throw e;
        }

        event.setAnnotation(eventDto.getAnnotation());
        event.setCategory(category);
        event.setConfirmedRequests(0);
        event.setCreatedOn(LocalDateTime.now());
        event.setDescription(eventDto.getDescription());
        event.setDateOfEvent(eventDateDto);
        event.setInitiator(initiator);
        event.setLocation(eventService.createLoc(loc));
        event.setPaid(eventDto.isPaid());
        event.setParticipantLimit(eventDto.getParticipantLimit());
        event.setRequestModeration(eventDto.isRequestModeration());
        event.setState(PENDING);
        event.setTitle(eventDto.getTitle());
        event.setViews(0);
        return event;
    }

    public Event convertFromUpdateEventRequest(long userId, UpdateEventRequest eventDto) {

        Event event = eventService.getByUserIdAndEventId(userId, eventDto.getEventId());
        LocalDateTime eventDateDto = LocalDateTime.parse(eventDto.getEventDate(), formatter);

        //изменить можно только отмененные события или события в состоянии ожидания модерации
        if (event.getState().equals(PUBLISHED)) {
            ValidationException e = new ValidationException("PUBLISHED event cannot be updated");
            log.error(e.getMessage());
            throw e;
        }
        //если редактируется отменённое событие, то оно автоматически переходит в состояние ожидания модерации
        if (event.getState().equals(CANCELED)) {
            event.setState(PENDING);
        }

        if (eventDto.getAnnotation() != null &&
                !eventDto.getAnnotation().equals(event.getAnnotation())) {
            event.setAnnotation(eventDto.getAnnotation());
        }

        if (eventDto.getCategory() != 0 &&
                eventDto.getCategory() != event.getCategory().getId()) {
            Category category = categoryService.getById(eventDto.getCategory());
            event.setCategory(category);
        }

        if (eventDto.getDescription() != null &&
                !eventDto.getDescription().equals(event.getDescription())) {
            event.setDescription(eventDto.getDescription());
        }

        if (eventDto.getEventDate() != null &&
                !eventDateDto.equals(event.getDateOfEvent())) {

            // дата и время на которые намечено событие не может быть раньше,
            // чем через два часа от текущего момента
            if (LocalDateTime.now().plus(2, ChronoUnit.HOURS).isAfter(eventDateDto)) {
                ValidationException e = new ValidationException("Попробуйте указать более позднее время и дату");
                log.error(e.getMessage());
                throw e;
            }
            event.setDateOfEvent(eventDateDto);
        }

        event.setPaid(eventDto.isPaid());

        if (eventDto.getParticipantLimit() != 0 &&
                eventDto.getParticipantLimit() != event.getParticipantLimit()) {
            event.setParticipantLimit(eventDto.getParticipantLimit());
        }

        if (eventDto.getTitle() != null &&
                !eventDto.getTitle().equals(event.getTitle())) {
            event.setTitle(eventDto.getTitle());
        }

        return event;
    }

    public Event convertFromAdminUpdateEventRequest(long eventId, AdminUpdateEventRequest eventDto) {

        Event event = eventService.getByEventId(eventId);
        Category category = categoryService.getById(eventDto.getCategory());
        LocalDateTime eventDateDto = LocalDateTime.parse(eventDto.getEventDate(), formatter);

        if (eventDto.getLocation() != null) {
            Location loc = new Location();
            loc.setLat(eventDto.getLocation().getLat());
            loc.setLon(eventDto.getLocation().getLon());
            event.setLocation(eventService.createLoc(loc));
        }

        event.setAnnotation(eventDto.getAnnotation());
        event.setCategory(category);
        event.setCreatedOn(LocalDateTime.now());
        event.setDescription(eventDto.getDescription());
        event.setDateOfEvent(eventDateDto);
        event.setPaid(eventDto.isPaid());
        event.setParticipantLimit(eventDto.getParticipantLimit());
        event.setRequestModeration(eventDto.isRequestModeration());
        event.setTitle(eventDto.getTitle());
        return event;
    }


}
