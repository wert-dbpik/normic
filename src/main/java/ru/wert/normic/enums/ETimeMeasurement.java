package ru.wert.normic.enums;

import lombok.Getter;

public enum ETimeMeasurement {

    SEC("сек"),
    MIN("мин");

    @Getter String name;

    ETimeMeasurement(String name) {
        this.name = name;
    }
}
