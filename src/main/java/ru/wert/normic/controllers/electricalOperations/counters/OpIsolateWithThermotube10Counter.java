package ru.wert.normic.controllers.electricalOperations.counters;

import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.electrical.OpIsolateWithThermTube10;
import ru.wert.normic.interfaces.NormCounter;

import static ru.wert.normic.AppStatics.CURRENT_BATCH;
import static ru.wert.normic.AppStatics.roundTo001;
import static ru.wert.normic.settings.NormConstants.ISOLATE_WITH_THERM_TUBE_10;

public class OpIsolateWithThermotube10Counter implements NormCounter{

    public OpData count(OpData data){
        OpIsolateWithThermTube10 opData = (OpIsolateWithThermTube10)data;

        int pins = opData.getPins(); //Количество наконечников

        //################################################################

        double timeOp =  pins * ISOLATE_WITH_THERM_TUBE_10; //мин

        double time = timeOp + timeOp * 0.084 + timeOp * 0.029 / CURRENT_BATCH;

        opData.setElectricalTime(roundTo001(time));
        return opData;
    }
}
