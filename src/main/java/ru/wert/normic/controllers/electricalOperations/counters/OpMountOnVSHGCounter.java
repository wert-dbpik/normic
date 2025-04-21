package ru.wert.normic.controllers.electricalOperations.counters;

import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.electrical.OpMountOnVSHG;
import ru.wert.normic.interfaces.NormCounter;

import static ru.wert.normic.AppStatics.roundTo001;
import static ru.wert.normic.settings.NormConstants.MOUNT_ON_VSHG;

public class OpMountOnVSHGCounter implements NormCounter{

    public OpData count(OpData data){
        OpMountOnVSHG opData = (OpMountOnVSHG)data;

        int elements = opData.getElements(); //Количество элементов'

        //################################################################

        double time =  elements * MOUNT_ON_VSHG;//мин

        opData.setElectricalTime(roundTo001(time));
        return opData;
    }
}
