package ru.wert.normic.enums;

import lombok.Getter;

import java.io.Serializable;

import static ru.wert.normic.entities.db_connection.constants.NormConstants.BIG_SAWING_SPEED;
import static ru.wert.normic.entities.db_connection.constants.NormConstants.SMALL_SAWING_SPEED;

public enum ESawType implements Serializable {

    SMALL_SAW("Малая", SMALL_SAWING_SPEED),
    BIG_SAW("Большая", BIG_SAWING_SPEED);

    @Getter private String name;
    @Getter private double speed;

    ESawType(String name, double speed) {
        this.name = name;
        this.speed = speed;
    }
}
