package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Email;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Getter
@Setter
public class User {

    public User(@Email @NotNull @NotBlank String email,
                @NotNull @NotBlank String login, String name, @Past LocalDate birthday) {
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }

    private int id;

    @Email
    @NotNull
    @NotBlank
    private String email;

    @NotNull
    @NotBlank
    private String login;

    private String name;

    @Past
    private LocalDate birthday;

    private Set<Integer> friends = new HashSet<>();
}