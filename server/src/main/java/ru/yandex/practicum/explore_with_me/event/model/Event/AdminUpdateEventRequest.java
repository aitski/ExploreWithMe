package ru.yandex.practicum.explore_with_me.event.model.Event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class AdminUpdateEventRequest {

    private String annotation;
    private long category;
    private String description;
    private String eventDate;
    private Location location;

    @Getter
    @Setter
    @ToString
    @AllArgsConstructor
    public static class Location {
        private float lat;
        private float lon;
    }

    private boolean paid;
    private int participantLimit;
    private boolean requestModeration;
    private String title;

}
