package ru.yandex.practicum.explore_with_me.event.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.explore_with_me.event.model.Location;

public interface LocationRepository extends JpaRepository<Location, Long> {

}
