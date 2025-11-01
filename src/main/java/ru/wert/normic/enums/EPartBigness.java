package ru.wert.normic.enums;

import lombok.Getter;

public enum EPartBigness {

    SMALL("Мелкая", 0.833), // 5/6 = 0.833
    BIG("Крупная", 1.0);    // 6/6 = 1.0


    @Getter String name;
    @Getter double time;


    EPartBigness(String name, double time) {
        this.name = name;
        this.time = time;
    }
}
