package ru.wert.normic.enums;

import lombok.Getter;

import java.io.Serializable;

public enum EMeasure implements Serializable {

    MEASURE_L50(50, 0.26),
    MEASURE_L80(80, 0.30),
    MEASURE_L130(130, 0.35),
    MEASURE_L200(200, 0.40),
    MEASURE_L300(300, 0.45),
    MEASURE_L500(500, 0.53),
    MEASURE_L800(800, 0.60),
    MEASURE_L1200(1200, 0.68);

    @Getter int length;
    @Getter double time;

    EMeasure(int length, double time){
        this.length = length;
        this.time = time;
    }
}
