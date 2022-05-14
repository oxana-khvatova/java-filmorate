package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeption.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.ValidationException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    private final Map<Integer, User> users = new HashMap<>();
    private static int idGenerator = 1;

    @Override
    public User addUser(User user) {
        if (user.getId() != 0) {
            log.error("Can't add user: validation failed id != 0");
            throw new ValidationException();
        }
        if (!checkUser(user)) {
            log.error("Can't add user: validation failed: user login contains space");
            throw new ValidationException();
        }
        if (!users.containsKey(user.getId())) {
            if (user.getName() == null || user.getName().isBlank()) {
                user.setName(user.getLogin());
            }
            user.setId(idGenerator++);
            users.put(user.getId(), user);
            log.info("Add user" + user);
            return user;
        } else {
            log.error("User already exist");
            throw new UserNotFoundException("User " + user + " not found ");
        }
    }

    public boolean checkUser(User user) {
        return !user.getLogin().contains(" ");
    }

    @Override
    public User updateUser(User newUser) {
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
            throw new UserNotFoundException("User " + newUser + " not found ");
        }
        return newUser;
    }

    @Override
    public Collection<User> findAllUsers() {
        return users.values();
    }

    @Override
    public void deleteUser(User user) {
        if (user.getId() == 0) {
            log.error("Can't add user: validation failed id != 0");
            throw new ValidationException();
        }
        if (users.containsKey(user.getId())) {
            users.remove(user.getId());
        } else {
            throw new UserNotFoundException("User " + user + " not found ");
        }
    }

    @Override
    public User findUserById(Integer id) {
        if (!users.containsKey(id)) {
            throw new UserNotFoundException("Пользователь с " + id + "не найден");
        }
        return users.get(id);
    }
}
