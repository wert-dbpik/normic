package ru.wert.normic.controllers.electricalOperations.counters;

import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.electrical.OpCutCableChannel;
import ru.wert.normic.interfaces.NormCounter;

import static ru.wert.normic.AppStatics.CURRENT_BATCH;
import static ru.wert.normic.AppStatics.roundTo001;
import static ru.wert.normic.settings.NormConstants.CUT_CABLE_CHANNEL;

public class OpCutCableChannelCounter implements NormCounter{

    public OpData count(OpData data){
        OpCutCableChannel opData = (OpCutCableChannel)data;

        int cuts = opData.getCuts(); //Количество резов'

        //################################################################

        double timeOp =  cuts * CUT_CABLE_CHANNEL; //мин

        double time = timeOp + timeOp * 0.084 + timeOp * 0.029 / CURRENT_BATCH;

        opData.setElectricalTime(roundTo001(time));
        return opData;
    }
}
