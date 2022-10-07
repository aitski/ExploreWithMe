package ru.yandex.practicum.ExploreWithMe.compilation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.ExploreWithMe.compilation.model.Compilation;
import ru.yandex.practicum.ExploreWithMe.compilation.storage.CompilationRepository;
import ru.yandex.practicum.ExploreWithMe.event.model.Event.Event;
import ru.yandex.practicum.ExploreWithMe.event.service.EventService;
import ru.yandex.practicum.ExploreWithMe.exception.exceptions.NotFoundException;
import ru.yandex.practicum.ExploreWithMe.exception.exceptions.ValidationException;

import java.util.List;
import java.util.Optional;


@Service
@Slf4j
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepository compilationRepository;
    private final EventService eventService;

    @Override
    public Compilation create(Compilation compilation) {

        Compilation compNew = compilationRepository.save(compilation);
        log.debug("Compilation created/updated: {}", compNew);
        return compNew;
    }

    @Override
    public void delete(long compId) {

        compilationRepository.deleteById(compId);
        log.debug("Compilation {} deleted", compId);
    }

    @Override
    public void deleteEventFromCompilation(long compId, long eventId) {

        Compilation compilation = getById(compId);
        List<Event> newList = compilation.getEvents();
        newList.removeIf(event -> event.getId().equals(eventId));
        compilation.setEvents(newList);
        compilationRepository.save(compilation);
        log.debug("Event {} from compilation {} deleted", eventId, compId);
    }

    @Override
    public void addEventToCompilation(long compId, long eventId) {

        Compilation compilation = getById(compId);
        Event event = eventService.getByEventId(eventId);
        List<Event> newList = compilation.getEvents();

        if (newList.stream().map(Event::getId).anyMatch(id -> id == eventId)) {
            ValidationException e = new ValidationException("событие уже есть в подборке");
            log.error(e.getMessage());
            throw e;
        }

        newList.add(event);
        compilation.setEvents(newList);
        compilationRepository.save(compilation);
        log.debug("Event {} to compilation {} added", eventId, compId);
    }

    @Override
    public void pin(long compId) {

        Compilation compilation = getById(compId);
        compilation.setPinned(true);
        compilationRepository.save(compilation);
        log.debug("Compilation {} pinned", compId);

    }

    @Override
    public void unpin(long compId) {

        Compilation compilation = getById(compId);
        compilation.setPinned(false);
        compilationRepository.save(compilation);
        log.debug("Compilation {} upinned", compId);
    }

    @Override
    public List<Compilation> getAll(Optional<Boolean> pinned, int from, int size) {

        List<Compilation> list;
        Pageable pageable = PageRequest.of(from / size, size);
        list = pinned.map
                (aBoolean -> compilationRepository.findAllByPinned(aBoolean, pageable).getContent()).
                orElseGet(() -> compilationRepository.findAll(pageable).getContent());

        log.debug("list of compilations returned: {}", list);
        return list;
    }

    public Compilation getById(long compId) {

        return compilationRepository.findById(compId).orElseThrow
                (() -> {
                    log.error("Compilation with id {} not found", compId);
                    return new NotFoundException();
                });
    }
}
