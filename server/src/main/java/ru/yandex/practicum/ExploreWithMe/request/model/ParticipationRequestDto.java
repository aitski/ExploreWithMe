package ru.yandex.practicum.ExploreWithMe.request.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.yandex.practicum.ExploreWithMe.event.model.Event.Event;
import ru.yandex.practicum.ExploreWithMe.event.model.State;
import ru.yandex.practicum.ExploreWithMe.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;
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
