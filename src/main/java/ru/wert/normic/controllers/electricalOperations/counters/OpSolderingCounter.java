package ru.wert.normic.controllers.electricalOperations.counters;

import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.electrical.OpSoldering;
import ru.wert.normic.interfaces.NormCounter;

import static ru.wert.normic.AppStatics.CURRENT_BATCH;
import static ru.wert.normic.AppStatics.roundTo001;
import static ru.wert.normic.settings.NormConstants.SOLDERING;

public class OpSolderingCounter implements NormCounter{

    public OpData count(OpData data){
        OpSoldering opData = (OpSoldering)data;

        int elements = opData.getElements(); //Количество элементов'

        //################################################################

        double timeOp =  elements * SOLDERING; //мин

        double time = timeOp + timeOp * 0.084 + timeOp * 0.029 / CURRENT_BATCH;

        opData.setElectricalTime(roundTo001(time));
        return opData;
    }
}
