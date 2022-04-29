package ru.yandex.practicum.filmorate.validators;


import org.springframework.stereotype.Service;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.time.Duration;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = PositiveDurationValidator.class)
@Documented
public @interface PositiveDuration {

    String message() default "{Duration.isNotPositive}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}

class PositiveDurationValidator implements ConstraintValidator<PositiveDuration, Duration> {
    @Override
    public boolean isValid(Duration value, ConstraintValidatorContext context) {
        return !value.isZero() && !value.isNegative();
    }
}