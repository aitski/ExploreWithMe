package ru.yandex.practicum.ExploreWithMe.event.model.Event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class NewEventDto {

    String annotation;
    long category;
    String description;
    String eventDate;
    Location location;
    long eventId;

    @Getter
    @Setter
    @ToString
    @AllArgsConstructor
    public static class Location {
        float lat;
        float lon;
    }

    boolean paid;
    int participantLimit;
    boolean requestModeration;
    String title;

}
