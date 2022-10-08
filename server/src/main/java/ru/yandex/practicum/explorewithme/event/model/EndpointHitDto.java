package ru.yandex.practicum.explorewithme.event.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class EndpointHitDto {

    private String app;
    private String uri;
    private String ip;
    private String timestamp;

}
