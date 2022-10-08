package ru.practicum.explorewithme.service;

import ru.practicum.explorewithme.model.EndpointHit;
import ru.practicum.explorewithme.model.ViewStats;

import java.util.List;

public interface StatsService {

    void save(EndpointHit endpointHit);

    List<ViewStats> get(String start, String end, String[] uris, boolean unique);
}
