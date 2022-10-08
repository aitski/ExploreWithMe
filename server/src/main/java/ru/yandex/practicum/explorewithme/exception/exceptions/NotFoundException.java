package ru.yandex.practicum.explorewithme.exception.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class NotFoundException extends RuntimeException  {

    private String ObjClass;
    private long id;
}