package ru.yandex.practicum.explorewithme.event.model.Event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class NewEventDto {

    private String annotation;
    private long category;
    private String description;
    private String eventDate;
    private Location location;
    private long eventId;

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
