package ru.yandex.practicum.ExploreWithMe.event.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.ExploreWithMe.event.EventMapper;
import ru.yandex.practicum.ExploreWithMe.event.model.Event.*;
import ru.yandex.practicum.ExploreWithMe.event.model.SortEnum;
import ru.yandex.practicum.ExploreWithMe.event.service.EventService;
import ru.yandex.practicum.ExploreWithMe.exception.exceptions.StateException;
import ru.yandex.practicum.ExploreWithMe.user.model.User;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("admin/events")
@RequiredArgsConstructor

public class EventControllerAdmin {

    private final EventService eventService;
    private final EventMapper eventMapper;

    @GetMapping
    public List<EventFullDto> getAll(@RequestParam Optional<Long[]> users,
                                     @RequestParam Optional<String[]> states,
                                     @RequestParam Optional<Long[]> categories,
                                     @RequestParam Optional<String> rangeStart,
                                     @RequestParam Optional<String> rangeEnd,
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
