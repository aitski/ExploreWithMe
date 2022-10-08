package ru.yandex.practicum.explorewithme.event.storage;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.explorewithme.event.model.Event.Event;
import ru.yandex.practicum.explorewithme.event.model.State;

import java.time.LocalDateTime;

public interface EventRepository extends JpaRepository<Event, Long> {

    Page<Event> findAllByDateOfEventIsAfterAndDateOfEventIsBeforeAndState (
            LocalDateTime start, LocalDateTime end, State state, Pageable pageable);
    Page<Event> findAllByInitiatorId(long userId, Pageable pageable);

    Page<Event> findAllByDateOfEventIsAfterAndDateOfEventIsBefore(
            LocalDateTime start, LocalDateTime end, Pageable pageable);

}