package ru.practicum.ExploreWithMe.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.ExploreWithMe.model.EndPointHitDto;
import ru.practicum.ExploreWithMe.model.EndpointHit;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatsMapper {

    public EndpointHit convertFromDto (EndPointHitDto endPointHitDto){

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime timestamp = LocalDateTime.parse(endPointHitDto.getTimestamp(),formatter);
        EndpointHit endpointHit = new EndpointHit();
        endpointHit.setApp(endPointHitDto.getApp());
        endpointHit.setIp(endPointHitDto.getIp());
        endpointHit.setUri(endPointHitDto.getUri());
        endpointHit.setTimestamp(timestamp);
        return endpointHit;
    }

}
