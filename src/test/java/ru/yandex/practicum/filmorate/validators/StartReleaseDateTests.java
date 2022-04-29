package ru.yandex.practicum.filmorate.validators;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.validation.*;
import java.time.LocalDate;

@Data
@AllArgsConstructor
class WithStartReleaseDate {
    @StartReleaseDate
    public LocalDate ld;
}

@SpringBootTest
public class StartReleaseDateTests {
    @Autowired
    private Validator validator;

    @Test
    void testStartReleaseDate() {
        WithStartReleaseDate now = new WithStartReleaseDate(LocalDate.now());
        WithStartReleaseDate invalid = new WithStartReleaseDate(LocalDate.of(1895, 12, 28));
        WithStartReleaseDate invalid2 = new WithStartReleaseDate(LocalDate.of(1195, 12, 28));
        Assertions.assertTrue(validator.validate(now).isEmpty());
        Assertions.assertFalse(validator.validate(invalid).isEmpty());
        Assertions.assertFalse(validator.validate(invalid2).isEmpty());
    }
}
