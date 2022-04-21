package ru.yandex.practicum.filmorate.controllers;

import org.slf4j.LoggerFactory;
import ru.yandex.practicum.filmorate.model.Film;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
public class FilmController {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(FilmController.class);
    private final Map<Integer, Film> films = new HashMap<>();
    private static int idGenerator = 1;

    @GetMapping("/films")
    public Map<Integer, Film> findAll() {
        return films;
    }

    @PutMapping("/films")
    public void updateFilm(@Valid @RequestBody Film film) {
        if (film.getID() == 0 || !checkFilm(film)) {
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
        if (film.getID() != 0 || !checkFilm(film)) {
            log.error("Can't add film: validation failed");
            throw new ValidationException();
        } else {
            film.setID(idGenerator++);
        }
        films.put(film.getID(), film);
        log.info("Add film" + film);
    }

    public boolean checkFilm(Film film) {
        return !film.getDuration().isNegative() &&
                !film.getDuration().isZero() &&
                !film.getName().isBlank() &&
                film.getDescription().length()<201 &&
                film.getReleaseDate().isAfter(LocalDate.of(1895, 12, 28));
    }
}
