package ru.wert.normic.enums;

import lombok.Getter;

import static ru.wert.normic.entities.settings.AppSettings.FRAME_SPEED;
import static ru.wert.normic.entities.settings.AppSettings.SOLID_BOX_SPEED;

public enum ELatheHoldType {

    CENTERS("В центрах"),
    HOLDER("В кулачках"),
    HOLDER_AND_CENTERS("С задним центром");


    @Getter String holderType;

    ELatheHoldType(String holderType) {
        this.holderType = holderType;
    }
}
