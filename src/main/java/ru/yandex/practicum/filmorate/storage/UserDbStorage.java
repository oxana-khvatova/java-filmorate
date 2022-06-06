package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeption.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

@Slf4j
@Component
@Primary
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public User add(User user) {
        String sql = "insert into users (email, login, name, birthday) values (?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"user_id"});
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getLogin());
            ps.setString(3, user.getName());
            ps.setDate(4, java.sql.Date.valueOf(user.getBirthday()));
            return ps;
        }, keyHolder);
        user.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
        return user;
    }

    @Override
    public void delete(User user) {
        String sqlQuery = "delete from users where user_id = ?";
        if (jdbcTemplate.update(sqlQuery, user.getId()) <= 0) {
            throw new UserNotFoundException("User " + user + " not found ");
        }
    }

    @Override
    public User update(User user) {
        String sqlQuery = "update users set email = ?, login = ?,name = ?, birthday = ? where user_id = ?";
        int updated = jdbcTemplate.update(sqlQuery,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId());
        if (updated <= 0) {
            throw new UserNotFoundException("User " + user + " not found ");
        }
        return user;
    }

    @Override
    public void addFriend(User user, User friend) {
        String sql = "insert into friend (user_id, friend_id, status_friendship) values (?, ?, ?)";
        jdbcTemplate.update(sql, user.getId(), friend.getId(), "ACCEPT");
    }

    @Override
    public void deleteFriends(User user, User friend) {
        String sql = "delete from friend where user_id = ? and friend_id = ?";
        if (jdbcTemplate.update(sql, user.getId(), friend.getId()) <= 0) {
            throw new UserNotFoundException("Friend " + friend + " not found ");
        }
    }

    @Override
    public Collection<User> findAll() {
        String sql = "select email, login, name, birthday, user_id from users";

        return jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs));
    }

    private User makeUser(ResultSet rs) throws SQLException {
        User user = new User(
                rs.getString("email"),
                rs.getString("login"),
                rs.getString("name"),
                rs.getDate("birthday").toLocalDate());
        user.setId(rs.getInt("user_id"));
        loadUserFriends(user);
        return user;
    }

    @Override
    public User findById(Integer id) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet
                ("select email, login, name, birthday from users where user_id = ?", id);
        if (userRows.next()) {
            User user = new User(
                    userRows.getString("email"),
                    userRows.getString("login"),
                    userRows.getString("name"),
                    userRows.getDate("birthday").toLocalDate());

            user.setId(id);
            loadUserFriends(user);
            log.info("Найден пользователь: {} {}", user.getId(), user.getLogin());
            return user;
        } else {
            log.info("Пользователь с идентификатором {} не найден.", id);
            throw new UserNotFoundException("Пользователь с идентификатором не найден");
        }
    }

    private void loadUserFriends(User user) {
        String sql = "select friend_id from friend where user_id=?";
        Collection<Integer> res = jdbcTemplate.query(sql, (rs, rowNum) -> rs.getInt("friend_id"),
                user.getId());
        user.setFriends(new HashSet<>(res));
    }
}
