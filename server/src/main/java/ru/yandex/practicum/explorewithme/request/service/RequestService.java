package ru.yandex.practicum.explorewithme.request.service;

import ru.yandex.practicum.explorewithme.request.model.Request;

import java.util.List;

public interface RequestService {

    List<Request> getAll(long userId);
    Request create(long userId, long eventId);
    Request cancel(long userId, long eventId);
    Request confirm(long userId, long eventId, long reqId);
    Request reject(long userId, long eventId, long reqId);
    List<Request>getAllByUserEvent (long userId, long eventId);

}
