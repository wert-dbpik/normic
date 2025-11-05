package ru.wert.normic.enums;

import lombok.Getter;

public enum EPartBigness {

    SMALL("Мелкая", 5, 0.833), // 5/6 = 0.833
    BIG("Крупная", 6, 1.0);    // 6/6 = 1.0


    @Getter String name;
    @Getter double time;
    @Getter double timePerPart;


    EPartBigness(String name, double time, double timePerPart) {
        this.name = name;
        this.time = time;
        this.timePerPart = timePerPart;
    }
}
