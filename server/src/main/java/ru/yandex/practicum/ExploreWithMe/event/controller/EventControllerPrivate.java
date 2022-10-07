package ru.yandex.practicum.ExploreWithMe.event.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.ExploreWithMe.event.EventMapper;
import ru.yandex.practicum.ExploreWithMe.event.model.Event.*;
import ru.yandex.practicum.ExploreWithMe.event.service.EventService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("users/{userId}/events")
@RequiredArgsConstructor

public class EventControllerPrivate {

    private final EventService eventService;
    private final EventMapper eventMapper;

    @GetMapping
    public List<EventShortDto> getAllByUser(@PathVariable long userId,
                                            @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                            @Positive @RequestParam(defaultValue = "10") int size) {

        List<Event> list = eventService.getAllByUser(userId, from, size);
        return list.stream()
                .map(eventMapper::convertToShortDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{eventId}")
    public EventFullDto getByEventIdAndUserId(@PathVariable long userId, @PathVariable long eventId) {
        return eventMapper.convertToFullDto(eventService.getByUserIdAndEventId(userId, eventId));
    }

    @PostMapping
    public EventFullDto create(@RequestBody NewEventDto event,
                               @PathVariable long userId) {

        return eventMapper.convertToFullDto(
                eventService.create(eventMapper.convertFromNewEventDto(userId, event))
        );
    }

    @PatchMapping
    public EventFullDto patch(@RequestBody UpdateEventRequest event,
                              @PathVariable long userId) {

        return eventMapper.convertToFullDto(
                eventService.create(eventMapper.convertFromUpdateEventRequest(userId, event))
        );
    }

    @PatchMapping("/{eventId}")
    public EventFullDto cancelEvent(@PathVariable long userId,
                                    @PathVariable long eventId) {

        return eventMapper.convertToFullDto(eventService.cancelEvent(userId, eventId));
    }

}
