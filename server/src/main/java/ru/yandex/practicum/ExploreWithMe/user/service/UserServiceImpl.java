package ru.yandex.practicum.ExploreWithMe.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.ExploreWithMe.exception.exceptions.NotFoundException;
import ru.yandex.practicum.ExploreWithMe.user.model.User;
import ru.yandex.practicum.ExploreWithMe.user.storage.UserRepository;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public List<User> getAll() {
        List<User> list = userRepository.findAll();
        log.debug("list of users returned: {}", list);
        return list;
    }

    @Override
    public User create(User user) {

        User userNew = userRepository.save(user);
        log.debug("user created: {}", userNew);
        return userNew;
    }

    @Override
    public void delete(long id) {
        userRepository.delete(getById(id));
        log.debug("user with id {} deleted", id);
    }
    @Override
    public User getById(long id) {

        User user = userRepository.findById(id).orElseThrow
                (() -> {
                    log.error("User with id {} not found", id);
                    return new NotFoundException();
                });
        log.debug("User returned {}",user);
        return user;
    }
}
