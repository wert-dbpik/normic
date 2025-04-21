package ru.wert.normic.controllers.electricalOperations.counters;

import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.electrical.OpMountOnScrewsNoDisAssm;
import ru.wert.normic.interfaces.NormCounter;

import static ru.wert.normic.AppStatics.CURRENT_BATCH;
import static ru.wert.normic.AppStatics.roundTo001;
import static ru.wert.normic.settings.NormConstants.*;

public class OpMountOnScrewsNoDisAssmCounter implements NormCounter{

    public OpData count(OpData data){
        OpMountOnScrewsNoDisAssm opData = (OpMountOnScrewsNoDisAssm)data;

        int twoScrews = opData.getTwoScrews(); //Количество креплений на 2 винта
        int fourScrews = opData.getFourScrews(); //Количество креплений на 4 винта
        boolean difficult = opData.isDifficult(); // Сложно устанавливать
        double k = difficult ? 1.3 : 1.0; //Коэффициент сложности

        //################################################################

        double timeOp =  twoScrews * MOUNT_ON_2_SCREWS_NO_DISASSM * k
                + fourScrews * MOUNT_ON_4_SCREWS_NO_DISASSM * k;   //мин

        double time = timeOp + timeOp * 0.064 + timeOp * 0.029 / CURRENT_BATCH;

        opData.setElectricalTime(roundTo001(time));
        return opData;
    }
}
