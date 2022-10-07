package ru.yandex.practicum.ExploreWithMe.compilation.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class ParticipationRequestDto {

    Long id;
    String created;
    Long event;
    Long requester;
    String status;

}
