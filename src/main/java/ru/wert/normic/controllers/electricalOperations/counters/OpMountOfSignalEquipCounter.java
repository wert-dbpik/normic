package ru.wert.normic.controllers.electricalOperations.counters;

import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.electrical.OpMountOfSignalEquip;
import ru.wert.normic.interfaces.NormCounter;

import static ru.wert.normic.AppStatics.CURRENT_BATCH;
import static ru.wert.normic.AppStatics.roundTo001;
import static ru.wert.normic.settings.NormConstants.MOUNT_OF_SIGNAL_EQUIP_SPEED;

public class OpMountOfSignalEquipCounter implements NormCounter{

    public OpData count(OpData data){
        OpMountOfSignalEquip opData = (OpMountOfSignalEquip)data;

        int elements = opData.getElements(); //Количество элементов'

        //################################################################

        double timeOp =  elements * MOUNT_OF_SIGNAL_EQUIP_SPEED;//мин

        double time = timeOp + timeOp * 0.084 + timeOp * 0.029 / CURRENT_BATCH;

        opData.setElectricalTime(roundTo001(time));
        return opData;
    }
}
