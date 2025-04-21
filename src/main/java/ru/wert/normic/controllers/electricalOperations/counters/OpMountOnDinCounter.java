package ru.wert.normic.controllers.electricalOperations.counters;

import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.electrical.OpMountOnDin;
import ru.wert.normic.entities.ops.opAssembling.OpAssmNut;
import ru.wert.normic.entities.ops.opLocksmith.OpChopOff;
import ru.wert.normic.enums.EMeasure;
import ru.wert.normic.interfaces.NormCounter;

import java.util.NoSuchElementException;

import static ru.wert.normic.AppStatics.roundTo001;
import static ru.wert.normic.controllers.AbstractOpPlate.SEC_TO_MIN;
import static ru.wert.normic.settings.NormConstants.*;
import static ru.wert.normic.settings.NormConstants.OTHERS_SPEED;

public class OpMountOnDinCounter implements NormCounter{

    public OpData count(OpData data){
        OpMountOnDin opData = (OpMountOnDin)data;

        int avtomats = opData.getAvtomats(); //Количество винтов
        int heaters = opData.getHeaters(); //Количество комплектов ВШГ

        //################################################################

        double time =  avtomats * MOUNT_ON_DIN_AVTOMATS
                + heaters * MOUNT_ON_DIN_HEATERS;   //мин

        opData.setElectricalTime(roundTo001(time));
        return opData;
    }
}
