package ru.yandex.practicum.explorewithme.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.explorewithme.request.model.ParticipationRequestDto;
import ru.yandex.practicum.explorewithme.request.model.Request;
@Service
@RequiredArgsConstructor
@Slf4j
public class RequestMapper {

    public ParticipationRequestDto convertToDto (Request request){

        return new ParticipationRequestDto(
                request.getId(),
                request.getCreated().toString(),
                request.getEvent().getId(),
                request.getRequester().getId(),
                request.getStatus().name()
        );
    }
}
