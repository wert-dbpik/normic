package ru.wert.normic.enums;

import lombok.Getter;

import java.util.NoSuchElementException;

import static ru.wert.normic.AppStatics.*;

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

    public static double getConsumption(EColor color){
        switch(color){
            case COLOR_NO: return 0.0;
            case COLOR_I: return CONSUMPTION_I * 0.001;
            case COLOR_II: return CONSUMPTION_II * 0.001;
            case COLOR_III: return CONSUMPTION_III * 0.001;
            default: throw new NoSuchElementException(String.format("Not found such EColor in the list %s", color.getName()));
        }
    }
}
