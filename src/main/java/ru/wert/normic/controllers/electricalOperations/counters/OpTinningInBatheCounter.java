package ru.wert.normic.controllers.electricalOperations.counters;

import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.electrical.OpSoldering;
import ru.wert.normic.entities.ops.electrical.OpTinningInBathe;
import ru.wert.normic.interfaces.NormCounter;

import static ru.wert.normic.AppStatics.CURRENT_BATCH;
import static ru.wert.normic.AppStatics.roundTo001;
import static ru.wert.normic.settings.NormConstants.SOLDERING_SPEED;
import static ru.wert.normic.settings.NormConstants.TINNING_IN_BATHE;

public class OpTinningInBatheCounter implements NormCounter{

    public OpData count(OpData data){
        OpTinningInBathe opData = (OpTinningInBathe)data;

        int pins = opData.getPins(); //Количество наконечников

        //################################################################

        double timeOp =  pins * TINNING_IN_BATHE; //мин

        double time = timeOp + timeOp * 0.084 + timeOp * 0.029 / CURRENT_BATCH;

        opData.setElectricalTime(roundTo001(time));
        return opData;
    }
}
