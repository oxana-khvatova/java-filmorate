package ru.yandex.practicum.filmorate.validators;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.time.LocalDate;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = StartReleaseDateValidator.class)
@Documented
public @interface StartReleaseDate {

    String message() default "{ReleaseDate.inValid}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

class StartReleaseDateValidator implements ConstraintValidator<StartReleaseDate, LocalDate> {
    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        return value.isAfter((LocalDate.of(1895, 12, 28)));
    }
}
