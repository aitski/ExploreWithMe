package ru.yandex.practicum.ExploreWithMe.request;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.ExploreWithMe.request.model.ParticipationRequestDto;
import ru.yandex.practicum.ExploreWithMe.request.model.Request;
import ru.yandex.practicum.ExploreWithMe.request.service.RequestService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users/{userId}")
@RequiredArgsConstructor
public class RequestController {

    private final RequestService requestService;
    private final RequestMapper requestMapper;

    //Запросы пользователя на участие в чужих событиях
    @GetMapping("/requests")
    public List<ParticipationRequestDto> getByAll(@PathVariable long userId) {
        List<Request> list = requestService.getAll(userId);
        return list.stream().map(requestMapper::convertToDto).collect(Collectors.toList());
    }

    //Создание запроса на участие в чужом событии
    @PostMapping("/requests")
    public ParticipationRequestDto create(@PathVariable long userId, @RequestParam long eventId) {
        return requestMapper.convertToDto(requestService.create(userId, eventId));
    }

    //Отмена своего запроса на участие в событии
    @PatchMapping("/requests/{requestId}/cancel")
    public ParticipationRequestDto cancel(@PathVariable long userId, @PathVariable long requestId) {
        return requestMapper.convertToDto(requestService.cancel(userId, requestId));
    }

    //Подтверждение заявки на участии в событии пользователя
    @PatchMapping("/events/{eventId}/requests/{reqId}/confirm")
    public ParticipationRequestDto confirm(
            @PathVariable long userId, @PathVariable long eventId, @PathVariable long reqId) {
        return requestMapper.convertToDto(requestService.confirm(userId, eventId, reqId));
    }

    //Отклонение заявки на участии в событии пользователя
    @PatchMapping("/events/{eventId}/requests/{reqId}/reject")
    public ParticipationRequestDto reject(
            @PathVariable long userId, @PathVariable long eventId, @PathVariable long reqId) {
        return requestMapper.convertToDto(requestService.reject(userId, eventId, reqId));
    }

    //Запросы на участие в событии пользователя
    @GetMapping("/events/{eventId}/requests")
    public List<ParticipationRequestDto> getAllByUserAndEvent(@PathVariable long userId,
                                                              @PathVariable long eventId) {
        List<Request> list = requestService.getAllByUserEvent(userId, eventId);
        return list.stream().map(requestMapper::convertToDto).collect(Collectors.toList());
    }
}
