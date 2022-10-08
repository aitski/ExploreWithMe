package ru.yandex.practicum.explorewithme.compilation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.explorewithme.compilation.model.Compilation;
import ru.yandex.practicum.explorewithme.compilation.model.CompilationDto;
import ru.yandex.practicum.explorewithme.compilation.model.NewCompilationDto;
import ru.yandex.practicum.explorewithme.event.EventMapper;
import ru.yandex.practicum.explorewithme.event.model.Event.Event;
import ru.yandex.practicum.explorewithme.event.model.Event.EventShortDto;
import ru.yandex.practicum.explorewithme.event.service.EventService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompilationMapper {

    private final EventService eventService;
    private final EventMapper eventMapper;

    public CompilationDto convertToDto(Compilation compilation) {

        List<EventShortDto> events = compilation.getEvents().
                stream().map(eventMapper::convertToShortDto).
                collect(Collectors.toList());

        return new CompilationDto(
                events, compilation.getId(),
                compilation.getPinned(), compilation.getTitle()
        );
    }

    public Compilation convertFromDto(NewCompilationDto compilationDto) {

        List<Long> list = compilationDto.getEvents();

        List<Event> events = list.stream().map(eventService::getByEventId).
                collect(Collectors.toList());

        Compilation compilation = new Compilation();
        compilation.setEvents(events);
        compilation.setPinned(compilationDto.isPinned());
        compilation.setTitle(compilationDto.getTitle());
        return compilation;
    }
}
