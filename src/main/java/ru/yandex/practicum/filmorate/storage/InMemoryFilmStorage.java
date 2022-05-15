package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeption.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.ValidationException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Integer, Film> films = new HashMap<>();
    private static int idGenerator = 1;

    @Override
    public Film add(Film film) {
        if (film.getID() != 0) {
            log.error("Can't add film: validation failed");
            throw new ValidationException();
        } else {
            film.setID(idGenerator++);
        }
        films.put(film.getID(), film);
        log.info("Add film: " + film);
        return film;
    }

    @Override
    public Film update(Film film) {
        if (film.getID() == 0) {
            log.error("Can't update film: validation failed");
            throw new ValidationException();
        }
        if (films.containsKey(film.getID())) {
            films.replace(film.getID(), film);
            log.info("Update film" + film);
            return film;
        } else {
            throw new FilmNotFoundException("Film not found");
        }
    }

    @Override
    public void delete(Film film) {
        if (film.getID() == 0) {
            log.error("Can't delete film: validation failed");
            throw new ValidationException();
        }
        if (films.containsKey(film.getID())) {
            films.remove(film.getID());
            log.info("Delete film: " + film);
        } else {
            throw new FilmNotFoundException("Film " + film + "not found");
        }
    }

    @Override
    public Collection<Film> findAll() {
        return films.values();
    }

    @Override
    public Film findById(Integer id) {
        if (!films.containsKey(id)) {
            throw new FilmNotFoundException("Film id " + id + "not found");
        }
        return films.get(id);
    }
}
