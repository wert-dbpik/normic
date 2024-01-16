package ru.wert.normic.controllers.listOperations.counters;

import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.opList.OpBending;
import ru.wert.normic.entities.ops.opList.OpCutting;
import ru.wert.normic.interfaces.NormCounter;

import static ru.wert.normic.settings.NormConstants.BENDING_SERVICE_RATIO;
import static ru.wert.normic.settings.NormConstants.BENDING_SPEED;

public class OpBendingCounter  implements NormCounter {

    public OpData count(OpData data){
        OpBending opData = (OpBending)data;

        int bends = opData.getBends(); //Количество гибов
        int men = opData.getMen(); //Количество рабочих
        double toolRatio = opData.getTool().getToolRatio(); //Коэффициент зависящий от гибочного станка

        //######################################################

        double time =  bends * BENDING_SPEED * toolRatio * men  //мин
                * BENDING_SERVICE_RATIO;

        opData.setMechTime(time);
        return opData;
    }

}
