package ru.yandex.practicum.explore_with_me.exception.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class StateOrStatusException extends RuntimeException  {

    private String message;
}