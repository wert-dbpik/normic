package ru.wert.normic.controllers.locksmith.counters;

import ru.wert.normic.entities.ops.opLocksmith.OpCutOffOnTheSaw;
import ru.wert.normic.enums.EMeasure;
import ru.wert.normic.enums.ESawType;

import java.util.NoSuchElementException;

public class OpCutOffOnTheSawCounter {

    public static OpCutOffOnTheSaw count(OpCutOffOnTheSaw opData){

        int length = opData.getLength();
        ESawType sawType = opData.getSaw();

        //######################################################

        double time = findTime(length) + sawType.getSpeed();

        opData.setMechTime(time);
        return opData;
    }

    private static Double findTime(int length){
        EMeasure lastMeasure = EMeasure.values()[EMeasure.values().length-1];
        if(length > lastMeasure.getLength())
            return lastMeasure.getTime();

        int prevL = 0;
        for (EMeasure d : EMeasure.values()) {
            if (length >= prevL && length <= d.getLength())
                return d.getTime();
            prevL = d.getLength();
        }

        throw new NoSuchElementException("Ошибка при определении значения нормы времени в таблице");
    }
}
