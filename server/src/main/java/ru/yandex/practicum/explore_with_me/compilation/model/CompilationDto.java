package ru.yandex.practicum.explore_with_me.compilation.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.yandex.practicum.explore_with_me.event.model.Event.EventShortDto;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor

public class CompilationDto {

    private List<EventShortDto> events;
    private Long id;
    private boolean pinned;
    private String title;

}
