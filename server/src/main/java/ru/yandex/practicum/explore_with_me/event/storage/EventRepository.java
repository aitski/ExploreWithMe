package ru.yandex.practicum.explore_with_me.event.storage;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.explore_with_me.event.model.Event.Event;
import ru.yandex.practicum.explore_with_me.event.model.State;

import java.time.LocalDateTime;

public interface EventRepository extends JpaRepository<Event, Long> {

    Page<Event> findAllByDateOfEventIsAfterAndDateOfEventIsBeforeAndState (
            LocalDateTime start, LocalDateTime end, State state, Pageable pageable);
    Page<Event> findAllByInitiatorId(long userId, Pageable pageable);

    Page<Event> findAllByDateOfEventIsAfterAndDateOfEventIsBefore(
            LocalDateTime start, LocalDateTime end, Pageable pageable);

}