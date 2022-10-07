package ru.yandex.practicum.ExploreWithMe.event.model.Event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class UpdateEventRequest {

    String annotation;
    long category;
    String description;
    String eventDate;
    long eventId;
    boolean paid;
    int participantLimit;
    String title;

}
