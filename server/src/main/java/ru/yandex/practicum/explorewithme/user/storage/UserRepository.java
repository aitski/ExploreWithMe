package ru.yandex.practicum.explorewithme.user.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.explorewithme.user.model.User;

public interface UserRepository extends JpaRepository<User,Long> {
}
