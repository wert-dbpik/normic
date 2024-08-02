package ru.wert.normic.enums;

import lombok.Getter;

public enum ENormType {

    NORM_MECHANICAL("МК"), //МК
    NORM_PAINTING("Покрасочный"), //ППК
    NORM_ASSEMBLING("Сборочный"), //СБОРКА
    NORM_PACKING("Упаковка"), //УПАКОВКА

    NORM_DETAIL("Деталь"), //Плашка из операций на деталь
    NORM_ASSEMBLE("Сборка"); //Плашка из операций на сборочную единицу

    @Getter
    private String normName;

    ENormType(String normName) {
        this.normName = normName;
    }

}
