package ru.yandex.practicum.filmorate.controllers;

import org.slf4j.LoggerFactory;
import ru.yandex.practicum.filmorate.model.User;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.time.LocalDate;
import java.util.*;

@RestController
public class UserController {

    private final Map<Integer, User> users = new HashMap<>();
    private static int idGenerator = 1;
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(UserController.class);

    @GetMapping("/users")
    public Map<Integer, User> findAllUsers() {
        return users;
    }

    @PutMapping("/users")
    public void updateUser(@Valid @RequestBody User newUser) {
        if (newUser.getId() == 0 || !checkUser(newUser)) {
            log.error("Can't update user: validation failed" + newUser);
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
        if (user.getId() != 0 || !checkUser(user)) {
            log.error("Can't add user: validation failed");
            throw new ValidationException();
        } else {
            user.setId(idGenerator++);
        }
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
        return user.getEmail().contains("@") && !user.getLogin().contains(" ") &&
                user.getLogin() != null &&
                user.getBirthday().isBefore(LocalDate.now());
    }
}