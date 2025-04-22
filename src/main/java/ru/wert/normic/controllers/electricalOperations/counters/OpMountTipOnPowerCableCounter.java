package ru.wert.normic.controllers.electricalOperations.counters;

import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.electrical.OpMountTipOnCable;
import ru.wert.normic.entities.ops.electrical.OpMountTipOnPowerCable;
import ru.wert.normic.interfaces.NormCounter;

import static ru.wert.normic.AppStatics.CURRENT_BATCH;
import static ru.wert.normic.AppStatics.roundTo001;
import static ru.wert.normic.settings.NormConstants.MOUNT_TIP_ON_CABLE;
import static ru.wert.normic.settings.NormConstants.MOUNT_TIP_ON_POWER_CABLE;

public class OpMountTipOnPowerCableCounter implements NormCounter{

    public OpData count(OpData data){
        OpMountTipOnPowerCable opData = (OpMountTipOnPowerCable)data;

        int pins = opData.getTips(); //Количество наконечников
        final int serviceTime = 2; //мин, время обслуживания на партию

        //################################################################

        double timeOp =  pins * MOUNT_TIP_ON_POWER_CABLE; //мин

        double time = timeOp + timeOp * 0.06 +
                (timeOp * serviceTime / CURRENT_BATCH) +
                (timeOp * 0.029 / CURRENT_BATCH);

        opData.setElectricalTime(roundTo001(time));
        return opData;
    }
}
