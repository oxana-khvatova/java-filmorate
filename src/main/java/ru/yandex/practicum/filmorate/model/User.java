package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Email;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class User{
    int id;

    @Email
    @NotNull
    @NotBlank
    String email;

    @NotNull
    @NotBlank
    String login;

    String name;

    @Past
    LocalDate birthday;
}