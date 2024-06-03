package ru.wert.normic.enums;


import lombok.Getter;

public enum EPieceMeasurement {

    PIECE("шт"),
    METER("м"),
    SQUARE_METER("м2"),
    CUB_METER("м3");

    @Getter
    String measureName;

    EPieceMeasurement(String measureName) {
        this.measureName = measureName;
    }
}
