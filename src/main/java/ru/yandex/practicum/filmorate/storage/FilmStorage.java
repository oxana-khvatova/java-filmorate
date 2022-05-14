package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {
    Film addFilm(Film film);

    void deleteFilm(Film film);

    Film updateFilm(Film film);

    Collection<Film> findAll();

    Film findFilmById(Integer id);
}
