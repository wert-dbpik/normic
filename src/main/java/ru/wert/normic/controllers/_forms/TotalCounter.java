package ru.wert.normic.controllers._forms;

import ru.wert.normic.controllers.AbstractOpPlate;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.interfaces.IOpWithOperations;

import java.util.List;

import static ru.wert.normic.AppStatics.MAIN_CONTROLLER;

public class TotalCounter {

    public static void recountNormsWithNewTotal(
            int total, //общее количество узлов в сборке
            OpData opData, //Текущий opData
            AbstractFormController thisController //контроллер формы
    ) {

        opData.setTotal(total);
        TotalCounter.recountTotals((IOpWithOperations) opData, opData.getTotal());
        //Пересчитывает все оперции в сборке исходя из нового общего количества
        for (OpData op : ((IOpWithOperations) opData).getOperations()) {
            if (op instanceof IOpWithOperations) continue;
            op.setTotal(opData.getTotal());
            op = op.getOpType().getNormCounter().count(op);
            AbstractOpPlate plate = op.getPlate();
            if (plate != null) plate.writeNormTime(op);
        }
        if (thisController instanceof FormAssmController)
            MAIN_CONTROLLER.recountNormTimes((IOpWithOperations) opData, opData.getTotal());
        if(thisController != null)
            thisController.countSumNormTimeByShops();
    }

    /**
     * Пересчитывает общее количество
     * @param opData
     * @param total
     */
    public static void recountTotals(IOpWithOperations opData, int total){
        List<OpData> ops = opData.getOperations();
        for(OpData op : ops){
            if(op instanceof IOpWithOperations) {
                int currentTotal = op.getQuantity() * total;
                op.setTotal(currentTotal);
                recountTotals((IOpWithOperations) op, currentTotal);
            } else {
                int currentTotal = op.getQuantity() * total;
                op.setTotal(currentTotal);
            }
        }
    }
}
