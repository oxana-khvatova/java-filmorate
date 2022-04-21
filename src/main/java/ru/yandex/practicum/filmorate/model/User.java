package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class User{
    int id;
    @NotBlank
    String email;
    @NotBlank
    String login;
    String name;
    LocalDate birthday;
}