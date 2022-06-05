package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exeption.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exeption.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.UserDbStorage;

import org.junit.jupiter.api.function.Executable;

import java.time.Duration;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmorateApplicationTests {
	private final UserDbStorage userStorage;
	private final FilmDbStorage filmStorage;
	private static User origUser;
	private static Film origFilm;
	@BeforeEach
	public void beforeEach() {
		origUser = new User("test@ya.ru", "test", "xxx",
				LocalDate.of(1990, 1, 1));
		origFilm = new Film("bestFilm", "test", LocalDate.of(1979, 6, 1),
				Duration.ofMinutes(123), Mpa.PG);
	}


	@Test
	public void testFindUserById() {
		userStorage.add(origUser);
		User loadedUser = userStorage.findById(origUser.getId());
		assertEquals(origUser.getId(), loadedUser.getId());
		assertEquals(origUser, loadedUser);
	}

	@Test
	public void testDeleteUser() {
		userStorage.add(origUser);
		userStorage.delete(origUser);
		UserNotFoundException exception = assertThrows(
				UserNotFoundException.class,
				new Executable() {
					@Override
					public void execute() {
						userStorage.findById(origUser.getId());
					}
				});
	}

	@Test
	public void testUpDateUserById() {
		userStorage.add(origUser);
		User origUser2 = new User("test@ya.ru", "test", "yyy",
				LocalDate.of(1990, 1, 1));
		origUser2.setId(origUser.getId());
		origUser = userStorage.update(origUser2);
		assertEquals("yyy", origUser.getName());
	}
	@Test
	public void testAddUserFriend() {
		userStorage.add(origUser);
		User origUser2 = new User("testorigUser2@ya.ru", "test2", "yyy",
				LocalDate.of(1987, 3, 1));
		userStorage.add(origUser2);
		userStorage.addFriend(origUser,origUser2);
		User origUser3 = userStorage.findById(origUser.getId());
		assertEquals(1, origUser3.getFriends().size());
	}
	@Test
	public void testFindFilmById() {
		filmStorage.add(origFilm);
		Film loadedFilm = filmStorage.findById(origFilm.getID());
		assertEquals(origFilm.getID(), loadedFilm.getID());
		assertEquals(origFilm, loadedFilm);
	}
	@Test
	public void testDeleteFilm() {
		filmStorage.add(origFilm);
		try {
			filmStorage.delete(origFilm);
		} catch (FilmNotFoundException exception) {
			exception = assertThrows(
					FilmNotFoundException.class,
					new Executable() {
						@Override
						public void execute() {
							filmStorage.findById(origFilm.getID());
						}
					});
		}
	}
	@Test
	public void testUpDateFilmById() {
		filmStorage.add(origFilm);
		Film origFilm2 = new Film("bestFilm2", "test",
				LocalDate.of(1979, 6, 1),
				Duration.ofMinutes(123), Mpa.PG);
		origFilm2.setID(origFilm.getID());
		origFilm = filmStorage.update(origFilm2);
		assertEquals("bestFilm2", origFilm.getName());
	}
	@Test
	public void testAddLikes() {
		filmStorage.add(origFilm);
		userStorage.add(origUser);
		filmStorage.addLike(origFilm,origUser);
		Film film = filmStorage.findById(origFilm.getID());
		assertEquals(1, film.getLikes().size());
	}
}