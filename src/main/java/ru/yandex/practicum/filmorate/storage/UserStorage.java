package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {
    User addUser(User user);

    void deleteUser(User user);

    User updateUser(User user);

    Collection<User> findAllUsers();

    User findUserById(Integer id);
}