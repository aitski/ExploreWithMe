package ru.yandex.practicum.explorewithme.exception.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class StateOrStatusException extends RuntimeException  {

    private String message;
}