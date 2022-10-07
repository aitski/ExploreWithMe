package ru.yandex.practicum.ExploreWithMe.event.model.Event;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class EventShortDto {

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

    boolean paid;
    String title;
    int views;
}
