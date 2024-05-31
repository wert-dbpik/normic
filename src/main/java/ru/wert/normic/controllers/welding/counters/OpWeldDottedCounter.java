package ru.wert.normic.controllers.welding.counters;

import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.opList.OpCutting;
import ru.wert.normic.entities.ops.opWelding.OpWeldDotted;
import ru.wert.normic.interfaces.NormCounter;

import static ru.wert.normic.AppStatics.roundTo001;
import static ru.wert.normic.settings.NormConstants.*;

public class OpWeldDottedCounter implements NormCounter {

    public OpData count(OpData data){
        OpWeldDotted opData = (OpWeldDotted)data;

        int parts = opData.getParts();
        int dots = opData.getDots();
        int drops = opData.getDrops();

        //######################################################
        double time =  parts * WELDING_PARTS_SPEED + dots * WELDING_DOTTED_SPEED + drops * WELDING_DROP_SPEED;   //мин

        opData.setMechTime(roundTo001(time));
        return opData;
    }
}
