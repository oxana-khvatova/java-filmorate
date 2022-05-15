package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.model.Film;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import javax.validation.*;
import java.util.Collection;
import java.util.List;

@Slf4j
@RestController
public class FilmController {

    FilmStorage filmStorage;
    FilmService filmService;

    @Autowired
    public FilmController(FilmStorage filmStorage, FilmService filmService) {
        this.filmStorage = filmStorage;
        this.filmService = filmService;
    }

    @GetMapping("/films")
    public Collection<Film> findAll() {
        return filmStorage.findAll();
    }

    @GetMapping("/films/{id}")
    public Film findById(@PathVariable("id") Integer id) {

        return filmStorage.findById(id);
    }

    @PutMapping("/films")
    public Film updateFilm(@Valid @RequestBody Film film) {
        return filmStorage.update(film);
    }

    @PostMapping("/films")
    public Film addFilm(@Valid @RequestBody Film film) {
        return filmStorage.add(film);
    }

    @DeleteMapping("/films")
    public void deleteFilm(@Valid @RequestBody Film film) {
        filmStorage.delete(film);
    }

    @PutMapping("/films/{id}/like/{userId}")
    public void addLike(@PathVariable("id") Integer id,
                        @PathVariable("userId") Integer friendId) {
        filmService.addLike(id, friendId);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public void deleteLike(@PathVariable("id") Integer id,
                           @PathVariable("userId") Integer friendId) {
        filmService.deleteLike(id, friendId);
    }

    @GetMapping("/films/popular")
    public List<Film> topFilms(@RequestParam(defaultValue = "10") Integer count) {
        return filmService.topFilms(count);
    }
}
