package ru.yandex.practicum.ExploreWithMe.exception.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class NotValidFieldException extends RuntimeException{
    public NotValidFieldException(String message) {
        super(message);
    }
}