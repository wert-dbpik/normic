package ru.wert.normic.enums;

import lombok.Getter;

import java.util.NoSuchElementException;

public enum ETimeMeasurement {

    SEC("сек"),
    MIN("мин");

    @Getter String measure;

    ETimeMeasurement(String measure) {
        this.measure = measure;
    }

    public static ETimeMeasurement findValueOf(String name){
        for(ETimeMeasurement m : ETimeMeasurement.values()){
            if(m.name().equals(name))
                return m;
        }

        throw new NoSuchElementException("No such measurement in ETimeMeasurement enum");
    }
}
