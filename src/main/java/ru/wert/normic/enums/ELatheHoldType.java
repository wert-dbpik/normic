package ru.wert.normic.enums;

import lombok.Getter;

public enum ELatheHoldType {

    CENTERS("В центрах"),
    HOLDER("В кулачках"),
    HOLDER_AND_CENTERS("С задним центром");


    @Getter String holderType;

    ELatheHoldType(String holderType) {
        this.holderType = holderType;
    }
}
