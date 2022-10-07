package ru.yandex.practicum.ExploreWithMe.event.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.ExploreWithMe.event.EventMapper;
import ru.yandex.practicum.ExploreWithMe.event.model.Event.Event;
import ru.yandex.practicum.ExploreWithMe.event.model.Event.EventFullDto;
import ru.yandex.practicum.ExploreWithMe.event.model.Event.EventShortDto;
import ru.yandex.practicum.ExploreWithMe.event.model.SortEnum;
import ru.yandex.practicum.ExploreWithMe.event.service.EventService;
import ru.yandex.practicum.ExploreWithMe.exception.exceptions.StateException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventControllerPublic {

    private final EventService eventService;
    private final EventMapper eventMapper;

    @GetMapping
    public List<EventShortDto> getAll(@RequestParam Optional<String> text,
                                      @RequestParam Optional<Long[]> categories,
                                      @RequestParam Optional<Boolean> paid,
                                      @RequestParam Optional<String> rangeStart,
                                      @RequestParam Optional<String> rangeEnd,
                                      @RequestParam(defaultValue = "false") boolean onlyAvailable,
                                      @RequestParam(defaultValue = "EVENT_DATE") String sortParam,
                                      @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                      @Positive @RequestParam(defaultValue = "10") int size,
                                      HttpServletRequest request) {

        SortEnum sortEnum = SortEnum.from(sortParam)
                .orElseThrow(() -> new StateException("sort", sortParam));

        List<Event> list = eventService.getAllPublic(
                text, categories, paid, rangeStart,
                rangeEnd, onlyAvailable, sortEnum, from, size, request);

        return list.stream()
                .map(eventMapper::convertToShortDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public EventFullDto getByEventId(@PathVariable long id, HttpServletRequest request) {
        return eventMapper.convertToFullDto(eventService.getByEventIdPublic(id, request));
    }

}
