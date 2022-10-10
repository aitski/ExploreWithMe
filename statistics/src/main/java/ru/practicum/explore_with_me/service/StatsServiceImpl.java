package ru.practicum.explore_with_me.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.explore_with_me.model.EndpointHit;
import ru.practicum.explore_with_me.model.ViewStats;
import ru.practicum.explore_with_me.storage.StatsRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {

    private final StatsRepository statsRepository;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public void save(EndpointHit endpointHit) {

        EndpointHit e = statsRepository.save(endpointHit);
        log.debug("stats saved: {}", e);
    }

    @Override
    public List<ViewStats> get(String start, String end, String[] uris, boolean unique) {
        List<ViewStats> list;

        if (unique) {
            list = statsRepository.countTotalperUriUniqueIp(LocalDateTime.parse(start, formatter),
                    LocalDateTime.parse(end, formatter));

        } else {
            list = statsRepository.countTotalperUri(LocalDateTime.parse(start, formatter),
                    LocalDateTime.parse(end, formatter));
        }

        if (uris.length != 0) {
            List<String> urisList = List.of(uris);
            list = list.stream().filter(v -> urisList.contains(v.getUri())).
                    collect(Collectors.toList());
        }
        return list;
    }
}
