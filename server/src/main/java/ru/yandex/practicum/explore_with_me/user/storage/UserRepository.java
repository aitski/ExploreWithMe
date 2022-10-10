package ru.yandex.practicum.explore_with_me.user.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.explore_with_me.user.model.User;

public interface UserRepository extends JpaRepository<User,Long> {
}
