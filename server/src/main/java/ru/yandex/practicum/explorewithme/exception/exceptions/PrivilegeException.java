package ru.yandex.practicum.explorewithme.exception.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PrivilegeException extends RuntimeException  {

    private String message;
}