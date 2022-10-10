package ru.yandex.practicum.explore_with_me.exception.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class NotFoundException extends RuntimeException  {

    private String ObjClass;
    private long id;
}