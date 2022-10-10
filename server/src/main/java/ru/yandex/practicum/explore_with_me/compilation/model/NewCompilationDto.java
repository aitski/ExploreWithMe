package ru.yandex.practicum.explore_with_me.compilation.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor

public class NewCompilationDto {

    private List<Long> events;
    private boolean pinned;
    private String title;

}
