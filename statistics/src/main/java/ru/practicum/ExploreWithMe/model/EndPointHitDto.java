package ru.practicum.ExploreWithMe.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class EndPointHitDto {

    String app;
    String uri;
    String ip;
    String timestamp;
}
