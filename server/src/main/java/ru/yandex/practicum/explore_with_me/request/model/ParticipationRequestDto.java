package ru.yandex.practicum.explore_with_me.request.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@Getter
@Setter
@ToString
@AllArgsConstructor
public class ParticipationRequestDto {

    private Long id;
    private String created;
    private Long event;
    private Long requester;
    private String status;

}
