package ru.wert.normic.controllers.electricalOperations.counters;

import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.electrical.OpIsolateWithThermTube30;
import ru.wert.normic.interfaces.NormCounter;

import static ru.wert.normic.AppStatics.CURRENT_BATCH;
import static ru.wert.normic.AppStatics.roundTo001;
import static ru.wert.normic.settings.NormConstants.ISOLATE_WITH_THERM_TUBE_30;

public class OpIsolateWithThermotube30Counter implements NormCounter{

    public OpData count(OpData data){
        OpIsolateWithThermTube30 opData = (OpIsolateWithThermTube30)data;

        int pins = opData.getPins(); //Количество наконечников

        //################################################################

        double timeOp =  pins * ISOLATE_WITH_THERM_TUBE_30; //мин

        double time = timeOp + timeOp * 0.084 + timeOp * 0.029 / CURRENT_BATCH;

        opData.setElectricalTime(roundTo001(time));
        return opData;
    }
}
