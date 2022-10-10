package ru.yandex.practicum.explore_with_me.event.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.explore_with_me.event.EventMapper;
import ru.yandex.practicum.explore_with_me.event.model.Event.AdminUpdateEventRequest;
import ru.yandex.practicum.explore_with_me.event.model.Event.Event;
import ru.yandex.practicum.explore_with_me.event.model.Event.EventFullDto;
import ru.yandex.practicum.explore_with_me.event.service.EventService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("admin/events")
@RequiredArgsConstructor

public class EventControllerAdmin {

    private final EventService eventService;
    private final EventMapper eventMapper;

    @GetMapping
    public List<EventFullDto> getAll(@RequestParam (required = false) Long[] users,
                                     @RequestParam (required = false) String [] states,
                                     @RequestParam (required = false) Long[] categories,
                                     @RequestParam (required = false) String rangeStart,
                                     @RequestParam (required = false) String rangeEnd,
                                     @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                     @Positive @RequestParam(defaultValue = "10") int size
    ) {

        List<Event> list = eventService.getAllAdmin(users, states, categories, rangeStart,
                rangeEnd, from, size);

        return list.stream()
                .map(eventMapper::convertToFullDto)
                .collect(Collectors.toList());
    }

    @PutMapping("/{eventId}")
    public EventFullDto put(@RequestBody AdminUpdateEventRequest event,
                            @PathVariable long eventId) {

        return eventMapper.convertToFullDto(
                eventService.create(eventMapper.convertFromAdminUpdateEventRequest(eventId, event))
        );
    }

    @PatchMapping("/{eventId}/publish")
    public EventFullDto publish(@PathVariable long eventId) {

        return eventMapper.convertToFullDto(eventService.publish(eventId));
    }

    @PatchMapping("/{eventId}/reject")
    public EventFullDto reject(@PathVariable long eventId) {

        return eventMapper.convertToFullDto(eventService.reject(eventId));
    }

}
