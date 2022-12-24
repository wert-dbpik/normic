package ru.wert.normic.enums;

import lombok.Getter;

public enum EAssemblingType {

    SOLID("Глухой шкаф", 1.686),
    FRAME("Рама (кросс)", 2.4);


    @Getter String name;
    @Getter Double speed;

    EAssemblingType(String name, Double speed) {
        this.name = name;
        this.speed = speed;
    }
}
