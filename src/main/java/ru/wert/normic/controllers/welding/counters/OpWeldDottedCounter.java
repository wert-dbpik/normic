package ru.wert.normic.controllers.welding.counters;

import ru.wert.normic.entities.ops.opWelding.OpWeldDotted;

import static ru.wert.normic.settings.NormConstants.*;

public class OpWeldDottedCounter {

    public static OpWeldDotted count(OpWeldDotted opData){

        int parts = opData.getParts();
        int dots = opData.getDots();
        int drops = opData.getDrops();

        //######################################################
        double time =  parts * WELDING_PARTS_SPEED + dots * WELDING_DOTTED_SPEED + drops * WELDING_DROP_SPEED;   //мин

        opData.setMechTime(time);
        return opData;
    }
}
