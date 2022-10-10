package ru.yandex.practicum.explore_with_me.event.model.Event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class UpdateEventRequest {

    private String annotation;
    private long category;
    private String description;
    private String eventDate;
    private long eventId;
    private boolean paid;
    private int participantLimit;
    private String title;

}
