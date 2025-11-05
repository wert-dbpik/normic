package ru.wert.normic.controllers.welding.counters;

import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.opWelding.OpWeldAssm;
import ru.wert.normic.enums.EPartBigness;
import ru.wert.normic.interfaces.NormCounter;

import static ru.wert.normic.AppStatics.*;

public class OpWeldAssmCounter implements NormCounter {

    public OpData count(OpData data){
        OpWeldAssm opData = (OpWeldAssm)data;

        EPartBigness bigness = opData.getPartBigness();
        int numOfDetails = opData.getNumOfDetails();

        //######################################################
        double t = bigness.getTimePerPart();

        double time = numOfDetails * t;   //мин

        opData.setMechTime(roundTo001(time));
        return opData;
    }
}
