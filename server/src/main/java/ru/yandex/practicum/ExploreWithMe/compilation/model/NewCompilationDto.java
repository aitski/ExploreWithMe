package ru.yandex.practicum.ExploreWithMe.compilation.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.yandex.practicum.ExploreWithMe.event.model.Event.EventShortDto;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor

public class NewCompilationDto {

    List<Long> events;
    boolean pinned;
    String title;

}
