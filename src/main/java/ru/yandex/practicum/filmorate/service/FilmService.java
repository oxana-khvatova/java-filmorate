package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {

    FilmStorage filmStorage;
    UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public void addLike(Integer id, Integer userId) {
        Film film = filmStorage.findById(id);
        User user = userStorage.findById(userId);
        filmStorage.addLike(film, user);
    }

    public void deleteLike(Integer id, Integer userId) {
        Film film = filmStorage.findById(id);
        User user = userStorage.findById(userId);
        filmStorage.deleteLike(film, user);
    }

    public List<Film> topFilms(Integer count) {
        return filmStorage.findAll().stream().
                sorted(Comparator.comparingInt(f -> -f.getLikes().size())).limit(count)
                .collect(Collectors.toList());
    }
}
