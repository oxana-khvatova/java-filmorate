package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface FilmStorage {
    Film add(Film film);

    void delete(Film film);

    Film update(Film film);

    void addLike(Film film, User user);

    void deleteLike (Film film, User user);

    Collection<Film> findAll();

    Film findById(Integer id);
}
