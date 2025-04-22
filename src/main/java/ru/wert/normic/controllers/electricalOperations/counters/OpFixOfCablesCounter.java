package ru.wert.normic.controllers.electricalOperations.counters;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.math3.analysis.function.Abs;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.electrical.OpFixOfCables;
import ru.wert.normic.entities.ops.electrical.OpMarking;
import ru.wert.normic.interfaces.NormCounter;

import static ru.wert.normic.AppStatics.CURRENT_BATCH;
import static ru.wert.normic.AppStatics.roundTo001;
import static ru.wert.normic.settings.NormConstants.FIX_OF_CABLES_SPEED;
import static ru.wert.normic.settings.NormConstants.MARKING_SPEED;

@Slf4j
public class OpFixOfCablesCounter implements NormCounter{

    public OpData count(OpData data){
        OpFixOfCables opData = (OpFixOfCables)data;

        int elements = (int) Math.round(opData.getLength() / 0.3); //Количество элементов'

        //################################################################

        double timeOp =  elements * FIX_OF_CABLES_SPEED;//мин

        double time = timeOp + timeOp * 0.084 + timeOp * 0.029 / CURRENT_BATCH;

        opData.setElectricalTime(roundTo001(time));
        return opData;
    }
}
