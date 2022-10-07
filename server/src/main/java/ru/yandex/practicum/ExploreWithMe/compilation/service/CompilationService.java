package ru.yandex.practicum.ExploreWithMe.compilation.service;

import ru.yandex.practicum.ExploreWithMe.compilation.model.Compilation;

import java.util.List;
import java.util.Optional;

public interface CompilationService {

    List<Compilation> getAll(Optional<Boolean> pinned, int from, int size);
    Compilation getById(long compId);
    Compilation create(Compilation compilation);
    void delete (long compId);
    void deleteEventFromCompilation(long compId,long eventId);
    void addEventToCompilation(long compId,long eventId);
    void pin (long compId);
    void unpin (long compId);
}
