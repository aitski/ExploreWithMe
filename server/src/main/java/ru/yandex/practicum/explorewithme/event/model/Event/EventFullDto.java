package ru.yandex.practicum.explorewithme.event.model.Event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class EventFullDto {

    String annotation;
    Category category;

    @Getter
    @Setter
    @ToString
    @AllArgsConstructor
    public static class Category {
        private long id;
        private String name;
    }

    private int confirmedRequests;
    private String createdOn;
    private String description;
    private String eventDate;
    private long id;
    private User initiator;

    @Getter
    @Setter
    @ToString
    @AllArgsConstructor
    public static class User {
        private long id;
        private String name;
    }

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
    private String publishedOn;
    private boolean requestModeration;
    private String state;
    private String title;
    private int views;
}
