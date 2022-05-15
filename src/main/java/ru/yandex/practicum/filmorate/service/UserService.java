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

    public void addFriends(Integer id, Integer friendId) {
        User user = userStorage.findById(id);
        User friendsUser = userStorage.findById(friendId);
        user.getFriends().add(friendId);
        log.info("Add friend");
        friendsUser.getFriends().add(id);
    }

    public void deleteFriends(Integer id, Integer friendId) {
        User user = userStorage.findById(id);
        User friendsUser = userStorage.findById(friendId);
        if (user.getFriends().contains(friendsUser.getId())) {
            user.getFriends().remove(friendsUser.getId());
            friendsUser.getFriends().remove(user.getId());
        } else {
            throw new UserNotFoundException("В списке друзей нет друга с id " + friendId);
        }
    }

    public Set<Integer> showCommonFriends(Integer id, Integer friendId) {
        User user = userStorage.findById(id);
        User friendsUser = userStorage.findById(friendId);
        Set<Integer> commonFriends = new HashSet<Integer>();
        for (Integer idUser : user.getFriends()) {
            if (friendsUser.getFriends().contains(idUser)) {
                commonFriends.add(idUser);
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
