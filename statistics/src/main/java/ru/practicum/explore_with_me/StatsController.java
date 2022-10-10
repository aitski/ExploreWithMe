package ru.practicum.explore_with_me;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore_with_me.model.EndPointHitDto;
import ru.practicum.explore_with_me.model.ViewStats;
import ru.practicum.explore_with_me.service.StatsMapper;
import ru.practicum.explore_with_me.service.StatsService;

import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class StatsController {

    private final StatsService service;
    private final StatsMapper statsMapper;

    @PostMapping("/hit")
    public void save(@RequestBody EndPointHitDto endpointHitDto) {

        service.save(statsMapper.convertFromDto(endpointHitDto));
    }

    @GetMapping("/stats")
    public List<ViewStats> get(@RequestParam String start, @RequestParam String end,
                               @RequestParam(required = false) String[] uris,
                               @RequestParam(defaultValue = "false") Boolean unique) {
        return service.get(start, end, uris, unique);
    }
}
