package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum Mpa {
    G(1, "G"),            // у фильма нет возрастных ограничений
    PG(2, "PG"),          // детям рекомендуется смотреть фильм с родителями
    PG13(3, "PG13"),      // детям до 13 лет просмотр не желателен
    R(4, "R"),            // лицам до 17 лет просматривать фильм можно только в присутствии взрослого
    NC17(5, "NC17");      // лицам до 18 лет просмотр запрещён ;

    @JsonProperty("id")
    private Integer id;
    @JsonProperty("name")
    private String name;


    Mpa(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @JsonCreator
    public static Mpa forValues(@JsonProperty("id") Integer id) {
        for (Mpa mpa : Mpa.values()) {
            if (mpa.id.equals(id))
                return mpa;
        }
        return null;
    }
}

