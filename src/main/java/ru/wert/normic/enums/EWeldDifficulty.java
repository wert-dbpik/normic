package ru.wert.normic.enums;

import lombok.Getter;

import java.util.NoSuchElementException;

public enum EWeldDifficulty {

    MIDDLE("средняя", 17),
    HEIGHT("сложная", 28);

    @Getter String difficultyName;
    @Getter int time;

    EWeldDifficulty(String difficultyName, int time) {
        this.difficultyName = difficultyName;
        this.time = time;
    }

    public static EWeldDifficulty findValueOf(String name){
        for(EWeldDifficulty m : EWeldDifficulty.values()){
            if(m.name().equals(name))
                return m;
        }

        throw new NoSuchElementException("No such measurement in ETimeMeasurement enum");
    }
}
