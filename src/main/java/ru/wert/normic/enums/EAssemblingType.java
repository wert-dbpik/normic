package ru.wert.normic.enums;

import lombok.Getter;

import static ru.wert.normic.entities.settings.NormConstants.FRAME_SPEED;
import static ru.wert.normic.entities.settings.NormConstants.SOLID_BOX_SPEED;

public enum EAssemblingType {

    SOLID("Глухой шкаф", SOLID_BOX_SPEED),
    FRAME("Рама (кросс)", FRAME_SPEED);


    @Getter String name;
    @Getter Double speed;

    EAssemblingType(String name, Double speed) {
        this.name = name;
        this.speed = speed;
    }
}
