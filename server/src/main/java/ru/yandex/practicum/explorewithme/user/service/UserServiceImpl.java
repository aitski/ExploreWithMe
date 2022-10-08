package ru.yandex.practicum.explorewithme.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.explorewithme.exception.exceptions.NotFoundException;
import ru.yandex.practicum.explorewithme.user.model.User;
import ru.yandex.practicum.explorewithme.user.storage.UserRepository;

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

        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User", id));
        log.debug("User returned {}", user);
        return user;
    }
}
