package ru.yandex.practicum.explorewithme.compilation.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.yandex.practicum.explorewithme.event.model.Event.EventShortDto;

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
