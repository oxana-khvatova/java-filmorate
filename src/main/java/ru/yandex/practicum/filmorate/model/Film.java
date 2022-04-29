package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.yandex.practicum.filmorate.validators.PositiveDuration;
import ru.yandex.practicum.filmorate.validators.StartReleaseDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import java.time.Duration;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class Film {
    int iD;

    @NotBlank
    String name;

    @Size(max = 200)
    String description;

    @StartReleaseDate
    LocalDate releaseDate;

    @PositiveDuration
    Duration duration;
}
