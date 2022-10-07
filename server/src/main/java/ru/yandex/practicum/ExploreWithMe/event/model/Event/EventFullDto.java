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
public class EventFullDto {

    String annotation;
    Category category;

    @Getter
    @Setter
    @ToString
    @AllArgsConstructor
    public static class Category {
        long id;
        String name;
    }

    int confirmedRequests;
    String createdOn;
    String description;
    String eventDate;
    long id;
    User initiator;

    @Getter
    @Setter
    @ToString
    @AllArgsConstructor
    public static class User {
        long id;
        String name;
    }

    Location location;

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
    String publishedOn;
    boolean requestModeration;
    String state;
    String title;
    int views;
}
