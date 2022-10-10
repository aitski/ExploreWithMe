package ru.yandex.practicum.explore_with_me.exception.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PrivilegeException extends RuntimeException  {

    private String message;
}