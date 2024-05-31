package ru.wert.normic.controllers.locksmith.counters;

import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.opList.OpCutting;
import ru.wert.normic.entities.ops.opLocksmith.OpChopOff;
import ru.wert.normic.enums.EMeasure;
import ru.wert.normic.interfaces.NormCounter;

import java.util.NoSuchElementException;

import static ru.wert.normic.AppStatics.roundTo001;

public class OpChopOffCounter implements NormCounter{

    private double chopTime = 0.05;

    public OpData count(OpData data){
        OpChopOff opData = (OpChopOff)data;

        int length = opData.getLength();

        //######################################################

        double time = findTime(length) + chopTime;

        opData.setMechTime(roundTo001(time));
        return opData;
    }

    private Double findTime(int length) {
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
