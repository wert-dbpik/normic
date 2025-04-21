package ru.wert.normic.controllers.electricalOperations.counters;

import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.electrical.OpMountOnScrewsWithDisAssm;
import ru.wert.normic.interfaces.NormCounter;

import static ru.wert.normic.AppStatics.roundTo001;
import static ru.wert.normic.settings.NormConstants.*;

public class OpMountOnScrewsWithDisAssmCounter implements NormCounter{

    public OpData count(OpData data){
        OpMountOnScrewsWithDisAssm opData = (OpMountOnScrewsWithDisAssm)data;

        int twoScrews = opData.getTwoScrews(); //Количество креплений на 2 винта
        int fourScrews = opData.getFourScrews(); //Количество креплений на 4 винта

        //################################################################

        double time =  twoScrews * MOUNT_ON_2_SCREWS_WITH_DISASSM
                + fourScrews * MOUNT_ON_4_SCREWS_WITH_DISASSM;   //мин

        opData.setElectricalTime(roundTo001(time));
        return opData;
    }
}
