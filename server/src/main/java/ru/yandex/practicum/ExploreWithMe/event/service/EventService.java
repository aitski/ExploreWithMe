package ru.yandex.practicum.ExploreWithMe.event.service;

import ru.yandex.practicum.ExploreWithMe.event.model.Event.Event;
import ru.yandex.practicum.ExploreWithMe.event.model.Location;
import ru.yandex.practicum.ExploreWithMe.event.model.SortEnum;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

public interface EventService {

    List<Event> getAllPublic(Optional<String> text, Optional<Long[]> categories,
                       Optional<Boolean> paid, Optional<String> rangeStart,
                       Optional<String> rangeEnd, boolean onlyAvailable, SortEnum sortEnum,
                       int from, int size, HttpServletRequest request);

    List<Event> getAllByUser(long userId, int from, int size);

    Event getByEventIdPublic(long id, HttpServletRequest request);
    Event getByEventId(long id);

    Event getByUserIdAndEventId(long userId, long eventId);

    Event create(Event event);

    Location createLoc(Location location);

    Event cancelEvent(long userId, long eventId);

    List<Event> getAllAdmin(Optional<Long[]> users, Optional<String[]> states, Optional<Long[]> categories,
                            Optional<String> rangeStart, Optional<String> rangeEnd, int from, int size);
    Event publish (long eventId);
    Event reject (long eventId);

}
