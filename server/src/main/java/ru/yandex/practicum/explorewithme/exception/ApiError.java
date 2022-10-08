package ru.yandex.practicum.explorewithme.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class ApiError {

    private StackTraceElement[] errors;
    private String message;
    private String reason;
    private String status;
    private String timestamp;

}
