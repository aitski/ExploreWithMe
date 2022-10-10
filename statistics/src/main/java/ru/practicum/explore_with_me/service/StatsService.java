package ru.practicum.explore_with_me.service;

import ru.practicum.explore_with_me.model.EndpointHit;
import ru.practicum.explore_with_me.model.ViewStats;

import java.util.List;

public interface StatsService {

    void save(EndpointHit endpointHit);

    List<ViewStats> get(String start, String end, String[] uris, boolean unique);
}
