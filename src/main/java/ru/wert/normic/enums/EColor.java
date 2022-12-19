package ru.wert.normic.enums;

import lombok.Getter;

public enum EColor {

    COLOR_NO("б/п"),
    COLOR_I("I"),
    COLOR_II("II"),
    COLOR_III("III");

    @Getter
    String name;

    EColor(String name) {
        this.name = name;
    }
}
