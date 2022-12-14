package ru.yandex.practicum.explore_with_me.compilation.service;

import ru.yandex.practicum.explore_with_me.compilation.model.Compilation;

import java.util.List;

public interface CompilationService {

    List<Compilation> getAll(Boolean pinned, int from, int size);
    Compilation getById(long compId);
    Compilation create(Compilation compilation);
    void delete (long compId);
    void deleteEventFromCompilation(long compId,long eventId);
    void addEventToCompilation(long compId,long eventId);
    void pin (long compId);
    void unpin (long compId);
}
