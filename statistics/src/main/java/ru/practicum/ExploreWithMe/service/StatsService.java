package ru.practicum.ExploreWithMe.service;

import ru.practicum.ExploreWithMe.model.*;

import java.util.List;
import java.util.Optional;

public interface StatsService {

    void save(EndpointHit endpointHit);

    List<ViewStats> get(String start, String end, Optional<String[]> uris, boolean unique);
}
