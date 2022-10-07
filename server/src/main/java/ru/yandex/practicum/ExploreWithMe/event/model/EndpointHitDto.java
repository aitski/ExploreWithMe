package ru.yandex.practicum.ExploreWithMe.event.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class EndpointHitDto {

    String app;
    String uri;
    String ip;
    String timestamp;

}
