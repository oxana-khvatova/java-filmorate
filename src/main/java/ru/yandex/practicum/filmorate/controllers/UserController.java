package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.model.User;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import javax.validation.Valid;
import java.util.*;

@Slf4j
@RestController
public class UserController {

    UserStorage userStorage;
    UserService userService;

    @Autowired
    public UserController(UserStorage userStorage, UserService userService) {
        this.userStorage = userStorage;
        this.userService = userService;
    }

    @GetMapping("/users")
    public Collection<User> findAllUsers() {
        return userStorage.findAll();
    }

    @GetMapping("/users/{id}")
    public User findUserById(@PathVariable("id") Integer id) {
        return userStorage.findById(id);
    }

    @PutMapping("/users")
    public User updateUser(@Valid @RequestBody User newUser) {
        return userStorage.update(newUser);
    }

    @PostMapping("/users")
    public User addUser(@Valid @RequestBody User user) {
        return userService.addUser(user);
    }

    @DeleteMapping("/users")
    public void deleteUser(@Valid @RequestBody User user) {
        userStorage.delete(user);
    }

    @PutMapping("/users/{id}/friends/{friendId}")
    public void addFriends(@PathVariable("id") Integer id,
                           @PathVariable("friendId") Integer friendId) {
        userService.addFriends(id, friendId);
    }

    @DeleteMapping("/users/{id}/friends/{friendId}")
    public void deleteFriends(@PathVariable("id") Integer id,
                              @PathVariable("friendId") Integer friendId) {
        userService.deleteFriends(id, friendId);
    }

    @GetMapping("/users/{id}/friends/common/{otherId}")
    public Set<User> showCommonFriends(@PathVariable("id") Integer id,
                                          @PathVariable("otherId") Integer otherId) {
        return userService.showCommonFriends(id, otherId);
    }

    @GetMapping("/users/{id}/friends")
    public Set<User> getFriends(@PathVariable("id") Integer id) {
        return userService.getFriends(id);
    }

}