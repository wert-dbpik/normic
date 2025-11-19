package ru.wert.normic.controllers.welding.counters;

import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.opWelding.OpWeldDotted;
import ru.wert.normic.interfaces.NormCounter;

import static ru.wert.normic.AppStatics.roundTo001;
import static ru.wert.normic.settings.NormConstants.*;

public class OpWeldDottedCounter implements NormCounter {

    public static double WELD_CAPACITOR_TPZ = 1.5; //Подготовительно заключительное время для конденсаторной оварки, мин
    public static double WELD_DOTTED_TPZ = 1.5; //Подготовительно заключительное время для сварки на точку, мин
    public static double WELD_DROPS_TPZ = 1.0; //Подготовительно заключительное время для сварки на прихватки, мин

    public OpData count(OpData data){
        OpWeldDotted opData = (OpWeldDotted)data;

        int parts = opData.getParts();
        int dots = opData.getDots();
        int drops = opData.getDrops();

        //######################################################
        double time =  parts * WELDING_CAPACITOR_SPEED + (parts == 0 ? 0 : WELD_CAPACITOR_TPZ) +
                dots * WELDING_DOTTED_SPEED + (dots == 0 ? 0 : WELD_DOTTED_TPZ) +
                drops * WELDING_DROP_SPEED + (drops == 0 ? 0 : WELD_DROPS_TPZ);   //мин

        opData.setMechTime(roundTo001(time));
        return opData;
    }
}
