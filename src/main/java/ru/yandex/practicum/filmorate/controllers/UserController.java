package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.model.User;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.util.*;

@Slf4j
@RestController
public class UserController {

    private final Map<Integer, User> users = new HashMap<>();
    private static int idGenerator = 1;

    @GetMapping("/users")
    public Collection<User> findAllUsers() {
        return users.values();
    }

    @PutMapping("/users")
    public void updateUser(@Valid @RequestBody User newUser) {
        if (newUser.getId() == 0) {
            log.error("Can't add user: validation failed id != 0");
            throw new ValidationException();
        }
        if (!checkUser(newUser)) {
            log.error("Can't add user: validation failed: user login contains space");
            throw new ValidationException();
        }
        if (users.containsKey(newUser.getId())) {
            users.replace(newUser.getId(), newUser);
            log.info("Updated user " + newUser);
        } else {
            throw new RuntimeException();
        }
    }

    @PostMapping("/users")
    public void addUser(@Valid @RequestBody User user) {
        if (user.getId() != 0) {
            log.error("Can't add user: validation failed id != 0");
            throw new ValidationException();
        }
        if (!checkUser(user)) {
            log.error("Can't add user: validation failed: user login contains space");
            throw new ValidationException();
        }
        user.setId(idGenerator++);
        if (!users.containsKey(user.getId())) {
            if (user.getName() == null || user.getName().isBlank()) {
                user.setName(user.getLogin());
            }
            users.put(user.getId(), user);
            log.info("Add user" + user);
        } else {
            log.error("User already exist");
            throw new RuntimeException();
        }
    }

    public boolean checkUser(User user) {
        return !user.getLogin().contains(" ");
    }
}