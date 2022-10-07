package ru.yandex.practicum.ExploreWithMe.event.model;

import java.util.Optional;

public enum SortEnum {
    EVENT_DATE,
    VIEWS;

    public static Optional<SortEnum> from(String stringSort) {
        for (SortEnum sortEnum : values()) {
            if (sortEnum.name().equalsIgnoreCase(stringSort)) {
                return java.util.Optional.of(sortEnum);
            }
        }
        return java.util.Optional.empty();
    }


}
