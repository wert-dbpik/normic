package ru.wert.normic.controllers.list.counters;

import ru.wert.normic.entities.ops.opList.OpBending;

import static ru.wert.normic.settings.NormConstants.BENDING_SERVICE_RATIO;
import static ru.wert.normic.settings.NormConstants.BENDING_SPEED;

public class OpBendingCounter {

    public static OpBending count(OpBending opData){

        Integer bends = opData.getBends(); //Количество гибов
        Integer men = opData.getMen(); //Количество рабочих
        double toolRatio = opData.getTool().getToolRatio(); //Коэффициент зависящий от гибочного станка

        //######################################################

        double time =  bends * BENDING_SPEED * toolRatio * men  //мин
                * BENDING_SERVICE_RATIO;

        opData.setMechTime(time);

        return opData;
    }

}
