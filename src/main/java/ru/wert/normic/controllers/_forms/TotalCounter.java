package ru.wert.normic.controllers._forms;

import ru.wert.normic.controllers.AbstractOpPlate;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.interfaces.IOpWithOperations;

import java.util.List;

import static ru.wert.normic.AppStatics.MAIN_CONTROLLER;
import static ru.wert.normic.AppStatics.roundTo001;

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

    public static OpData countSumNormTimeByShops(IOpWithOperations opData, AbstractFormController prevAssmController){

        double mechanicalTime = 0.0;
        double paintingTime = 0.0;
        double assemblingTime = 0.0;
        double packingTime = 0.0;

        for(OpData cn: opData.getOperations()){
            mechanicalTime += cn.getMechTime() * cn.getQuantity();
            paintingTime += cn.getPaintTime() * cn.getQuantity();
            assemblingTime += cn.getAssmTime() * cn.getQuantity();
            packingTime += cn.getPackTime() * cn.getQuantity();
        }

        ((OpData)opData).setMechTime(roundTo001(mechanicalTime));
        ((OpData)opData).setPaintTime(roundTo001(paintingTime));
        ((OpData)opData).setAssmTime(roundTo001(assemblingTime));
        ((OpData)opData).setPackTime(roundTo001(packingTime));

        if(prevAssmController != null)
            prevAssmController.countSumNormTimeByShops();

        return (OpData) opData;
    }
}
