package ru.yandex.practicum.explore_with_me.event.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.explore_with_me.event.EventMapper;
import ru.yandex.practicum.explore_with_me.event.model.Event.Event;
import ru.yandex.practicum.explore_with_me.event.model.Event.EventFullDto;
import ru.yandex.practicum.explore_with_me.event.model.Event.EventShortDto;
import ru.yandex.practicum.explore_with_me.event.model.SortEnum;
import ru.yandex.practicum.explore_with_me.event.service.EventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ValidationException;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventControllerPublic {

    private final EventService eventService;
    private final EventMapper eventMapper;
    @GetMapping
    public List<EventShortDto> getAll(@RequestParam(required = false) String text,
                                      @RequestParam(required = false) Long[] categories,
                                      @RequestParam(required = false) Boolean paid,
                                      @RequestParam(required = false) String rangeStart,
                                      @RequestParam(required = false) String rangeEnd,
                                      @RequestParam(defaultValue = "false") boolean onlyAvailable,
                                      @RequestParam(defaultValue = "EVENT_DATE") String sortParam,
                                      @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                      @Positive @RequestParam(defaultValue = "10") int size,
                                      HttpServletRequest request) {

        SortEnum sortEnum = SortEnum.from(sortParam)
                .orElseThrow(() -> new ValidationException("Неправильный параметр сортировки"));

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
