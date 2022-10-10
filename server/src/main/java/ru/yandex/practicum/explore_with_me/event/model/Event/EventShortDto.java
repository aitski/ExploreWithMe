package ru.yandex.practicum.explore_with_me.event.model.Event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class EventShortDto {

    private String annotation;
    private Category category;

    @Getter
    @Setter
    @ToString
    @AllArgsConstructor
    public static class Category {
        private long id;
        private String name;
    }

    private int confirmedRequests;
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

    private boolean paid;
    private String title;
    private int views;
}
