package ru.wert.normic.enums;

import lombok.Getter;

import java.util.NoSuchElementException;

import static ru.wert.normic.controllers.AbstractOpPlate.MIN_TO_HOUR;
import static ru.wert.normic.controllers.AbstractOpPlate.MIN_TO_SEC;

public enum ETimeMeasurement {

    SEC("сек", MIN_TO_SEC),
    MIN("мин", 1.0),
    HOUR("н.ч", MIN_TO_HOUR);

    @Getter String measure;
    @Getter Double rate;

    ETimeMeasurement(String measure, double rate) {
        this.measure = measure;
        this.rate = rate;
    }

    public static ETimeMeasurement findValueOf(String name){
        for(ETimeMeasurement m : ETimeMeasurement.values()){
            if(m.name().equals(name))
                return m;
        }

        throw new NoSuchElementException("No such measurement in ETimeMeasurement enum");
    }
}
