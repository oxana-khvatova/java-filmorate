package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeption.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exeption.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

@Slf4j
@Component
@Primary
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film add(Film film) {
        String sql = "insert into film (name, description, release_date, duration, mpa) values (?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"film_id"});
            ps.setString(1, film.getName());
            ps.setString(2, film.getDescription());
            ps.setDate(3, java.sql.Date.valueOf(film.getReleaseDate()));
            ps.setLong(4, film.getDuration().toSeconds());
            ps.setString(5, film.getMpa().name());
            return ps;
        }, keyHolder);

        film.setID(Objects.requireNonNull(keyHolder.getKey()).intValue());
        return film;
    }

    @Override
    public void delete(Film film) {
        String sqlQuery = "delete from film where film_id = ?";
        if (jdbcTemplate.update(sqlQuery, film.getID()) > 0) {
            throw new FilmNotFoundException("User " + film + " not found ");
        }
    }

    @Override
    public Film update(Film film) {
        String sqlQuery = "update film set name = ?, description = ?, release_date = ?, duration = ?, " +
                "mpa = ? where film_id = ?";
        int updated = jdbcTemplate.update(sqlQuery,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration().toSeconds(),
                film.getMpa().toString(),
                film.getID());
        if (updated <= 0) {
            throw new UserNotFoundException("User " + film + " not found ");
        }
        return film;
    }

    @Override
    public void addLike(Film film, User user) {
        String sql = "insert into likes (user_id, film_id) values (?, ?)";
        jdbcTemplate.update(sql, user.getId(), film.getID());
    }

    @Override
    public void deleteLike(Film film, User user) {
        String sql = "delete from likes where user_id = ? and film_id = ?";
        if (jdbcTemplate.update(sql, user.getId(), film.getID()) <= 0) {
            throw new UserNotFoundException("Film " + film + " not found ");
        }
    }

    @Override
    public Collection<Film> findAll() {
        String sql = "select film_id, name, description, release_date, duration, mpa from film";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs));
    }

    private Film makeFilm(ResultSet rs) throws SQLException {
        Film film = new Film(
                rs.getString("name"),
                rs.getString("description"),
                rs.getDate("release_date").toLocalDate(),
                Duration.ofSeconds(rs.getLong("duration")),
                Mpa.valueOf(rs.getString("mpa")));
        film.setID(rs.getInt("film_id"));
        loadFilmLikes(film);
        return film;
    }

    @Override
    public Film findById(Integer id) {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet
                ("select name, description, release_date, duration, mpa from film where film_id = ?", id);
        if (filmRows.next()) {
            Film film = new Film(
                    filmRows.getString("name"),
                    filmRows.getString("description"),
                    filmRows.getDate("release_date").toLocalDate(),
                    Duration.ofSeconds(filmRows.getLong("duration")),
                    Mpa.valueOf(filmRows.getString("mpa")));
                    film.setID(id);
            loadFilmLikes(film);
            log.info("Найден фильм: {} {}", film.getID(), film.getName());
            return film;
        } else {
            log.info("Фильм с идентификатором {} не найден.", id);
            throw new FilmNotFoundException("Фильм с идентификатором не найден");
        }
    }

    private void loadFilmLikes(Film film) {
        String sql = "select user_id from likes where film_id=?";
        Collection<Integer> res = jdbcTemplate.query(sql, (rs, rowNum) -> rs.getInt("user_id"),
                film.getID());
        film.setLikes(new HashSet<>(res));
    }
}
