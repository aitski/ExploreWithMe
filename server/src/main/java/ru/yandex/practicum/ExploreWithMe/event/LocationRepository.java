package ru.yandex.practicum.ExploreWithMe.event;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.ExploreWithMe.event.model.Location;

public interface LocationRepository extends JpaRepository<Location, Long> {

}
