package ru.yandex.practicum.explore_with_me.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.explore_with_me.event.model.Event.Event;
import ru.yandex.practicum.explore_with_me.event.service.EventService;
import ru.yandex.practicum.explore_with_me.exception.exceptions.*;
import ru.yandex.practicum.explore_with_me.request.model.Request;
import ru.yandex.practicum.explore_with_me.request.storage.RequestRepository;
import ru.yandex.practicum.explore_with_me.user.model.User;
import ru.yandex.practicum.explore_with_me.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

import static ru.yandex.practicum.explore_with_me.event.model.State.PUBLISHED;
import static ru.yandex.practicum.explore_with_me.request.model.Status.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final UserService userService;
    private final EventService eventService;

    @Override
    public List<Request> getAll(long userId) {

        List<Request> list = requestRepository.findAllByRequesterId(userId);
        log.debug("list of requests returned: {}", list);
        return list;
    }

    @Override
    public Request create(long userId, long eventId) {

        User requester = userService.getById(userId);
        Event event = eventService.getByEventId(eventId);
        List<Request> list = getAll(userId);

        if (list.stream().map(request -> request.getEvent().getId()).anyMatch(id -> id == eventId)) {
            ValidationException e = new ValidationException("нельзя добавить повторный запрос");
            log.error(e.getMessage());
            throw e;
        }

        if (event.getInitiator().getId() == userId) {
            PrivilegeException e = new PrivilegeException
                    ("инициатор события не может добавить запрос на участие в своём событии");
            log.error(e.getMessage());
            throw e;
        }

        if (!event.getState().equals(PUBLISHED)) {
            StateOrStatusException e = new StateOrStatusException
                    ("нельзя участвовать в неопубликованном событии");
            log.error(e.getMessage());
            throw e;
        }

        if (event.getParticipantLimit() != 0 &&
                event.getParticipantLimit().equals(event.getConfirmedRequests())) {
            ExceedLimitException e = new ExceedLimitException
                    ("достигнут лимит запросов на участие");
            log.error(e.getMessage());
            throw e;
        }

        Request request = new Request();
        //если для события отключена пре-модерация запросов на участие,
        //то запрос должен автоматически перейти в состояние подтвержденного
        if (!event.getRequestModeration()) {
            request.setStatus(CONFIRMED);
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
            eventService.create(event);
        } else {
            request.setStatus(PENDING);
        }

        request.setCreated(LocalDateTime.now());
        request.setRequester(requester);
        request.setEvent(event);
        Request requestNew = requestRepository.save(request);
        log.debug("Request created/updated by user {} event {}: {}",
                userId, eventId, requestNew);
        return requestNew;
    }

    @Override
    public Request cancel(long userId, long requestId) {

        Request request = getById(requestId);
        Event event = request.getEvent();

        if (request.getRequester().getId() != userId) {
            PrivilegeException e = new PrivilegeException
                    ("запрос на событие не принадлежит пользователю");
            log.error(e.getMessage());
            throw e;
        }

        request.setStatus(CANCELED);
        event.setConfirmedRequests(event.getConfirmedRequests() - 1);
        Request requestNew = requestRepository.save(request);
        log.debug("Request cancelled: {}", requestNew);
        return requestNew;
    }

    @Override
    public Request confirm(long userId, long eventId, long requestId) {

        Request request = getById(requestId);
        Event event = request.getEvent();

        if (event.getInitiator().getId() != userId) {
            PrivilegeException e = new PrivilegeException
                    ("событие не принадлежит пользователю");
            log.error(e.getMessage());
            throw e;
        }

        if (request.getRequester().getId() == userId) {
            PrivilegeException e = new PrivilegeException
                    ("нельзя подтверждать свой запрос");
            log.error(e.getMessage());
            throw e;
        }

        if (request.getStatus().equals(CONFIRMED)) {
            StateOrStatusException e = new StateOrStatusException
                    ("Подтверждение не требуется");
            log.error(e.getMessage());
            throw e;
        }

        if (event.getParticipantLimit().equals(event.getConfirmedRequests())) {
            ExceedLimitException e = new ExceedLimitException
                    ("достигнут лимит запросов на участие");
            log.error(e.getMessage());
            throw e;
        }

        request.setStatus(CONFIRMED);
        event.setConfirmedRequests(event.getConfirmedRequests() + 1);
        Request requestNew = requestRepository.save(request);
        log.debug("Request confirmed: {}", requestNew);

        //если при подтверждении данной заявки, лимит заявок для события исчерпан,
        // то все неподтверждённые заявки необходимо отклонить
        if (event.getParticipantLimit().equals(event.getConfirmedRequests())) {

            List<Request> list = getAll(userId);
            list.stream().filter(r -> r.getStatus().equals(PENDING)).
                    forEach(r -> r.setStatus(REJECTED));
        }
        return requestNew;
    }

    @Override
    public Request reject(long userId, long eventId, long requestId) {

        Request request = getById(requestId);
        Event event = request.getEvent();

        if (event.getInitiator().getId() != userId) {
            PrivilegeException e = new PrivilegeException
                    ("событие не принадлежит пользователю");
            log.error(e.getMessage());
            throw e;
        }

        request.setStatus(REJECTED);
        Request requestNew = requestRepository.save(request);
        log.debug("Request rejected: {}", requestNew);
        return requestNew;
    }

    public List<Request> getAllByUserEvent(long userId, long eventId) {

        Event event = eventService.getByEventId(eventId);

        if (event.getInitiator().getId() != userId) {
            PrivilegeException e = new PrivilegeException
                    ("событие не принадлежит пользователю");
            log.error(e.getMessage());
            throw e;
        }

        List<Request> list = requestRepository.findAllByEventId(eventId);
        log.debug("list of requests returned for user {}, event {}: {}",
                userId, eventId, list);
        return list;
    }

    private Request getById(long id) {

        Request request = requestRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Request", id));
        log.debug("Request returned {}", request);
        return request;
    }
}
