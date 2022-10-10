package ru.yandex.practicum.explore_with_me.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.explore_with_me.user.model.User;
import ru.yandex.practicum.explore_with_me.user.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<User> getAll() {
        return userService.getAll();
    }

    @PostMapping
    public User create(@RequestBody User user) {
        return userService.create(user);
    }

    @DeleteMapping("/{userId}")
    public void delete(@PathVariable long userId) {
        userService.delete(userId);
    }

}
