package ru.yandex.practicum.explorewithme.event.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.explorewithme.event.model.Location;

public interface LocationRepository extends JpaRepository<Location, Long> {

}
