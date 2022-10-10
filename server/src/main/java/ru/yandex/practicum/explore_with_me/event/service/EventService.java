package ru.yandex.practicum.explore_with_me.event.service;

import ru.yandex.practicum.explore_with_me.event.model.Event.Event;
import ru.yandex.practicum.explore_with_me.event.model.Location;
import ru.yandex.practicum.explore_with_me.event.model.SortEnum;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface EventService {

    List<Event> getAllPublic(String text, Long[] categories,
                             Boolean paid, String rangeStart,
                             String rangeEnd, boolean onlyAvailable, SortEnum sortEnum,
                             int from, int size, HttpServletRequest request);

    List<Event> getAllByUser(long userId, int from, int size);

    Event getByEventIdPublic(long id, HttpServletRequest request);
    Event getByEventId(long id);

    Event getByUserIdAndEventId(long userId, long eventId);

    Event create(Event event);

    Location createLoc(Location location);

    Event cancelEvent(long userId, long eventId);

    List<Event> getAllAdmin(Long[] users, String[] states, Long[] categories,
                            String rangeStart, String rangeEnd, int from, int size);
    Event publish (long eventId);
    Event reject (long eventId);

}
