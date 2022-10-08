package ru.yandex.practicum.explorewithme.request.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.explorewithme.request.model.Request;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request,Long> {

    List<Request> findAllByRequesterId (long userId);
    List<Request> findAllByEventId (long eventId);


}
