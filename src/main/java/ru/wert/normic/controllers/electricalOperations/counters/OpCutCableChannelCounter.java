package ru.wert.normic.controllers.electricalOperations.counters;

import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.electrical.OpCutCableChannel;
import ru.wert.normic.entities.ops.electrical.OpCutMetalSleeve;
import ru.wert.normic.interfaces.NormCounter;

import static ru.wert.normic.AppStatics.CURRENT_BATCH;
import static ru.wert.normic.AppStatics.roundTo001;
import static ru.wert.normic.settings.NormConstants.CUTTING_CABLE_CHANNEL;
import static ru.wert.normic.settings.NormConstants.CUTTING_METAL_SLEEVE;

public class OpCutCableChannelCounter implements NormCounter{

    public OpData count(OpData data){
        OpCutCableChannel opData = (OpCutCableChannel)data;

        int cuts = opData.getCuts(); //Количество резов'

        //################################################################

        double timeOp =  cuts * CUTTING_CABLE_CHANNEL; //мин

        double time = timeOp + timeOp * 0.064 + timeOp * 0.029 / CURRENT_BATCH;

        opData.setElectricalTime(roundTo001(time));
        return opData;
    }
}
