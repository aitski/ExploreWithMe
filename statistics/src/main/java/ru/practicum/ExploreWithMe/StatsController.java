package ru.practicum.ExploreWithMe;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ExploreWithMe.model.*;
import ru.practicum.ExploreWithMe.service.*;

import java.util.List;
import java.util.Optional;

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
                               @RequestParam Optional<String[]> uris,
                               @RequestParam(defaultValue = "false") Boolean unique) {
        return service.get(start, end, uris, unique);
    }
}
