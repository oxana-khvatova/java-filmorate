package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import lombok.Getter;
import lombok.Setter;
import ru.yandex.practicum.filmorate.validators.PositiveDuration;
import ru.yandex.practicum.filmorate.validators.StartReleaseDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import java.time.Duration;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Getter
@Setter
public class Film {

    public Film(@NotBlank String name, @Size
            (max = 200) String description,
                LocalDate releaseDate, Duration duration, Mpa mpa) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.mpa = mpa;
    }

    private Mpa mpa;

    private int iD;

    @NotBlank
    private String name;

    @NotBlank
    @Size(max = 200)
    private String description;

    @StartReleaseDate
    private LocalDate releaseDate;

    @PositiveDuration
    private Duration duration;

    private Set<Integer> likes = new HashSet<>();
}
