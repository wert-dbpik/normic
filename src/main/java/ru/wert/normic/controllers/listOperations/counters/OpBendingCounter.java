package ru.wert.normic.controllers.listOperations.counters;

import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.opList.OpBending;
import ru.wert.normic.interfaces.NormCounter;

import static ru.wert.normic.AppStatics.*;
import static ru.wert.normic.settings.NormConstants.BENDING_SERVICE_RATIO;
import static ru.wert.normic.settings.NormConstants.BENDING_SPEED;

public class OpBendingCounter  implements NormCounter {

    public OpData count(OpData data){
        OpBending opData = (OpBending)data;

        int bends = opData.getBends(); //Количество гибов
        int men = opData.getMen(); //Количество рабочих
        double toolRatio = opData.getTool().getToolRatio(); //Коэффициент зависящий от гибочного станка

        double timePZ = opData.isDifficult() ? 12.0 : 3.0; //Подготовительно заключительное время от сложности

        //######################################################

        double operationTime =  bends * BENDING_SPEED * toolRatio * men  * BENDING_SERVICE_RATIO; //Оперативное время
        double time = operationTime;
        if(BATCHNESS.get()) time = time +
//                0.1 * operationTime +
                timePZ / CURRENT_BATCH / opData.getTotal();

        opData.setMechTime(roundTo001(time));
        return opData;
    }

}
