package ru.wert.normic.controllers.electricalOperations.counters;

import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.electrical.OpMountOnScrewsWithDisAssm4;
import ru.wert.normic.interfaces.NormCounter;

import static ru.wert.normic.AppStatics.CURRENT_BATCH;
import static ru.wert.normic.AppStatics.roundTo001;
import static ru.wert.normic.settings.NormConstants.MOUNT_ON_SCREWS_WITH_DISASSM_4;

public class OpMountOnScrewsWithDisAssm4Counter implements NormCounter{

    public OpData count(OpData data){
        OpMountOnScrewsWithDisAssm4 opData = (OpMountOnScrewsWithDisAssm4)data;

        int fourScrews = opData.getFourScrews(); //Количество креплений на 4 винта
        boolean difficult = opData.isDifficult(); // Сложно устанавливать
        double k = difficult ? 1.3 : 1.0; //Коэффициент сложности

        //################################################################

        double timeOp =  fourScrews * MOUNT_ON_SCREWS_WITH_DISASSM_4 * k;   //мин

        double time = timeOp + timeOp * 0.084 + timeOp * 0.029 / CURRENT_BATCH;

        opData.setElectricalTime(roundTo001(time));
        return opData;
    }
}
