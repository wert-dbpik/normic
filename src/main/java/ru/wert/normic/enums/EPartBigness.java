package ru.wert.normic.enums;

import lombok.Getter;

public enum EPartBigness {

    SMALL("Мелкая", 5.0),
    BIG("Крупная", 6.0);


    @Getter String name;
    @Getter double time;


    EPartBigness(String name, double time) {
        this.name = name;
        this.time = time;
    }
}
