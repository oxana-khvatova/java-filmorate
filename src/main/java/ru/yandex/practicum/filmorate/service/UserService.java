package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.exeption.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@Service
public class UserService {

    UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User addUser(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        return userStorage.add(user);
    }

    public void addFriends(Integer id, Integer friendId) {
        User user = userStorage.findById(id);
        User friendsUser = userStorage.findById(friendId);
        userStorage.addFriend(user, friendsUser);
        log.info("Add friend");
    }

    public void deleteFriends(Integer id, Integer friendId) {
        User user = userStorage.findById(id);
        User friendsUser = userStorage.findById(friendId);
        userStorage.deleteFriends(user, friendsUser);
        log.info("Delete friend");
    }

    public Set<User> showCommonFriends(Integer id, Integer friendId) {
        User user = userStorage.findById(id);
        User friendsUser = userStorage.findById(friendId);
        Set<User> commonFriends = new HashSet<>();
        for (Integer idUser : user.getFriends()) {
            if (friendsUser.getFriends().contains(idUser)) {
                commonFriends.add(userStorage.findById(idUser));
            }
        }
        return commonFriends;
    }

    public Set<User> getFriends(Integer id) {
        User user = userStorage.findById(id);
        Set<User> friendsUser = new HashSet<>();
        for (Integer friendId : user.getFriends()) {
            friendsUser.add(userStorage.findById(friendId));
        }
        return friendsUser;
    }
}
