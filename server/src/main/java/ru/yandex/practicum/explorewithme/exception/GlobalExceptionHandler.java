package ru.yandex.practicum.explorewithme.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.explorewithme.exception.exceptions.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    //400 Запрос составлен с ошибкой
    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(BAD_REQUEST)
    public ApiError handleValidationException(ValidationException e) {

        return new ApiError(e.getStackTrace(), e.getMessage(), "400 Запрос составлен с ошибкой",
                (BAD_REQUEST.name()), LocalDateTime.now().format(formatter));
    }

    //403 Не выполнены условия для совершения операции
    @ExceptionHandler(PrivilegeException.class)
    @ResponseStatus(FORBIDDEN)
    public ApiError handlePrivilegeException(PrivilegeException e) {

        return new ApiError(e.getStackTrace(), e.getMessage(),
                "403 Не выполнены условия для совершения операции",
                (FORBIDDEN.name()), LocalDateTime.now().format(formatter));
    }

    //404 Объект не найден
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public ApiError handleNotFoundException(NotFoundException e) {

        return new ApiError(e.getStackTrace(), String.format("%s with id %s not found", e.getObjClass(), e.getId()),
                "404 Объект не найден", NOT_FOUND.name(), LocalDateTime.now().format(formatter));
    }


    //409 Запрос приводит к нарушению целостности данных
    @ExceptionHandler({org.hibernate.exception.ConstraintViolationException.class,
            javax.validation.ConstraintViolationException.class})
    @ResponseStatus(CONFLICT)
    public ApiError handleConstraintViolation(RuntimeException e) {

        return new ApiError(e.getStackTrace(), e.getMessage(),
                "409 Запрос приводит к нарушению целостности данных",
                (CONFLICT.name()), LocalDateTime.now().format(formatter));
    }

    //500 Внутренняя ошибка сервера
    @ExceptionHandler({ExceedLimitException.class, StateOrStatusException.class})
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public ApiError handleExceedLimitException(RuntimeException e) {

        return new ApiError(e.getStackTrace(), e.getMessage(), "500 Внутренняя ошибка сервера",
                (INTERNAL_SERVER_ERROR.name()), LocalDateTime.now().format(formatter));
    }
}
