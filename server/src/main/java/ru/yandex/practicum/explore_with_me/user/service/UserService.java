package ru.yandex.practicum.explore_with_me.user.service;

import ru.yandex.practicum.explore_with_me.user.model.User;

import java.util.List;

public interface UserService {

    List<User> getAll();
    User create(User user);
    void delete (long id);
    User getById(long id);

}
