package ru.yandex.practicum.ExploreWithMe.user.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.ExploreWithMe.user.model.User;

public interface UserRepository extends JpaRepository<User,Long> {
}
