package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.model.Film;
import org.springframework.web.bind.annotation.*;

import javax.validation.*;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
public class FilmController {

    private final Map<Integer, Film> films = new HashMap<>();
    private static int idGenerator = 1;

    @GetMapping("/films")
    public Collection<Film> findAll() {
        return films.values();
    }

    @PutMapping("/films")
    public void updateFilm(@Valid @RequestBody Film film) {
        if (film.getID() == 0) {
            log.error("Can't add film: validation failed");
            throw new ValidationException();
        }
        if (films.containsKey(film.getID())) {
            films.replace(film.getID(), film);
            log.info("Update film" + film);
        } else {
            throw new RuntimeException();
        }
    }

    @PostMapping("/films")
    public void addFilm(@Valid @RequestBody Film film) {
        if (film.getID() != 0) {
            log.error("Can't add film: validation failed");
            throw new ValidationException();
        } else {
            film.setID(idGenerator++);
        }
        films.put(film.getID(), film);
        log.info("Add film: " + film);
    }
}
