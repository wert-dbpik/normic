package ru.wert.normic.controllers.welding.counters;

import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.opWelding.OpWeldDifficulty;
import ru.wert.normic.entities.ops.opWelding.OpWeldDotted;
import ru.wert.normic.enums.EWeldDifficulty;
import ru.wert.normic.interfaces.NormCounter;

import static ru.wert.normic.AppStatics.*;
import static ru.wert.normic.settings.NormConstants.*;

public class OpWeldDifficultyCounter implements NormCounter {

    public OpData count(OpData data){
        OpWeldDifficulty opData = (OpWeldDifficulty)data;

        EWeldDifficulty difficulty = opData.getDifficulty();

        //######################################################
        double d = difficulty.getTime();
        double t = CURRENT_BATCH;

        double time = d / t;   //мин

        if(!BATCHNESS.get()) time = 0.0;
        opData.setMechTime(roundTo001(time));
        return opData;
    }
}
