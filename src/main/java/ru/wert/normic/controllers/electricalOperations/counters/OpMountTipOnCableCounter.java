package ru.wert.normic.controllers.electricalOperations.counters;

import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.electrical.OpMountTipOnCable;
import ru.wert.normic.entities.ops.electrical.OpTinning;
import ru.wert.normic.interfaces.NormCounter;

import static ru.wert.normic.AppStatics.CURRENT_BATCH;
import static ru.wert.normic.AppStatics.roundTo001;
import static ru.wert.normic.settings.NormConstants.MOUNT_TIP_ON_CABLE;
import static ru.wert.normic.settings.NormConstants.TINNING;

public class OpMountTipOnCableCounter implements NormCounter{

    public OpData count(OpData data){
        OpMountTipOnCable opData = (OpMountTipOnCable)data;

        int pins = opData.getTips(); //Количество наконечников

        //################################################################

        double timeOp =  pins * MOUNT_TIP_ON_CABLE; //мин

        double time = timeOp + timeOp * 0.084 + timeOp * 0.029 / CURRENT_BATCH;

        opData.setElectricalTime(roundTo001(time));
        return opData;
    }
}
