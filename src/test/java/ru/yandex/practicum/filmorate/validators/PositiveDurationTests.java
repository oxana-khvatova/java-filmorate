package ru.yandex.practicum.filmorate.validators;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.validation.*;
import java.time.Duration;

@Data
@AllArgsConstructor
class TestClass {
    @PositiveDuration
    public Duration duration;
}

@SpringBootTest
public class PositiveDurationTests {
    @Autowired
    private Validator validator;

    @Test
    void testPositiveDuration() {
        TestClass positive = new TestClass(Duration.ofSeconds(1));
        TestClass zero = new TestClass(Duration.ofSeconds(0));
        TestClass negative = new TestClass(Duration.ofSeconds(-1));


        Assertions.assertTrue(validator.validate(positive).isEmpty());
        Assertions.assertFalse(validator.validate(zero).isEmpty());
        Assertions.assertFalse(validator.validate(negative).isEmpty());
    }
}
