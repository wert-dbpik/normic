package ru.wert.normic.enums;

import lombok.Getter;

import java.util.NoSuchElementException;

public enum EMaterialMeasurement {

    M2("м2"),
    M3("м3");

    @Getter String measure;

    EMaterialMeasurement(String measure) {
        this.measure = measure;
    }

    public static EMaterialMeasurement findValueOf(String name){
        for(EMaterialMeasurement m : EMaterialMeasurement.values()){
            if(m.name().equals(name))
                return m;
        }

        throw new NoSuchElementException("No such measurement in ETimeMeasurement enum");
    }
}
