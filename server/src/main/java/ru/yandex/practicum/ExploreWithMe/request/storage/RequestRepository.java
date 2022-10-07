package ru.yandex.practicum.ExploreWithMe.request.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.ExploreWithMe.request.model.Request;
import ru.yandex.practicum.ExploreWithMe.user.model.User;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request,Long> {

    List<Request> findAllByRequester_Id (long userId);
    List<Request> findAllByEvent_Id (long eventId);


}
