package ru.wert.normic.controllers._forms;

import ru.wert.normic.controllers.AbstractOpPlate;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.interfaces.IOpWithOperations;

import java.util.List;

import static java.lang.String.format;
import static ru.wert.normic.AppStatics.MAIN_CONTROLLER;
import static ru.wert.normic.AppStatics.roundTo001;
import static ru.wert.normic.controllers.AbstractOpPlate.DOUBLE_FORMAT;
import static ru.wert.normic.controllers.AbstractOpPlate.MIN_TO_HOUR;

public class TotalCounter {

//    public static void recountNormsWithNewTotal(
//            int total, //общее количество узлов в сборке
//            OpData opData, //Текущий opData
//            AbstractFormController thisController //контроллер формы
//    ) {
//
//        opData.setTotal(total);
//        TotalCounter.recountTotals((IOpWithOperations) opData, opData.getTotal());
//        //Пересчитывает все оперции в сборке исходя из нового общего количества
//        for (OpData op : ((IOpWithOperations) opData).getOperations()) {
//            if (op instanceof IOpWithOperations) continue;
//            op.setTotal(opData.getTotal());
//            op = op.getOpType().getNormCounter().count(op);
//            AbstractOpPlate plate = op.getPlate();
//            if (plate != null) plate.writeNormTime(op);
//        }
//        if (thisController instanceof FormAssmController)
////            recountNormTimes((IOpWithOperations) opData, opData.getTotal());
//        if(thisController != null)
//            thisController.countSumNormTimeByShops();
//    }
//
//    /**
//     * Пересчитывает общее количество
//     * @param opData
//     * @param total
//     */
//    public static void recountTotals(IOpWithOperations opData, int total){
//        List<OpData> ops = opData.getOperations();
//        for(OpData op : ops){
//            if(op instanceof IOpWithOperations) {
//                int currentTotal = op.getQuantity() * total;
//                op.setTotal(currentTotal);
//                recountTotals((IOpWithOperations) op, currentTotal);
//            } else {
//                int currentTotal = op.getQuantity() * total;
//                op.setTotal(currentTotal);
//            }
//        }
//    }

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

    /**
     * Пересчет норм времени по подразделениям МК, Покраска и т.д.
     * Результаты суммируются и на выходе имеем измененный opsData
     * Параллельно происходит изменение переменной total,
     * которая отвечает за уменьшение нормы с увеличением количества узлов в изделии
     */
    public IOpWithOperations recountNormTimes(IOpWithOperations opsData, int amount) {

        double tempMechTime = 0.0;
        double tempPaintTime = 0.0;
        double tempAssmTime = 0.0;
        double tempPackTime = 0.0;

        //Для нашей ДЕТАЛИ или СБОРКИ получаем список оперций
        for (OpData op : opsData.getOperations()) {

            if (op instanceof IOpWithOperations) {//Если входящая операция ДЕТАЛЬ или СБОРКА

                op.setTotal(amount * op.getQuantity());
                //Рекурсивно вызываем нашу функцию
                op = (OpData) recountNormTimes((IOpWithOperations) op, amount * op.getQuantity());

                tempMechTime += op.getMechTime() * op.getQuantity();
                tempPaintTime += op.getPaintTime() * op.getQuantity();
                tempAssmTime += op.getAssmTime() * op.getQuantity();
                tempPackTime += op.getPackTime() * op.getQuantity();

            } else {//Простая операция

                op = op.getOpType().getNormCounter().count(op); //Вычисляем, аргуметом передаем саму операцию

                //Если операция отображается на плашке, то
                op.setTotal(amount * op.getQuantity());
                AbstractOpPlate plate = op.getPlate();
                if(plate != null)
                    plate.writeNormTime(op); //заполняем ее tfNormTime обновленными данными

                //Прибавляем нормы времени этой операции к остальным нормам ДЕТАЛИ или СБОРКИ
                tempMechTime += op.getMechTime();
                tempPaintTime += op.getPaintTime();
                tempAssmTime += op.getAssmTime();
                tempPackTime += op.getPackTime();
            }
        }

        ((OpData) opsData).setMechTime(tempMechTime);
        ((OpData) opsData).setPaintTime(tempPaintTime);
        ((OpData) opsData).setAssmTime(tempAssmTime);
        ((OpData) opsData).setPackTime(tempPackTime);

        //Заполняем окно нашей ДЕТАЛИ или СБОРКИ если оно отображается
        AbstractFormController formController = opsData.getFormController();
        if(formController != null)
            formController.writeNormTime((OpData) opsData);

        System.out.println("Наименование : " + opsData.getName());
        System.out.println("formController : " + opsData.getFormController());
        System.out.println("Количество   : " + ((OpData) opsData).getQuantity());
        System.out.println("Норма времени : " + format(DOUBLE_FORMAT, ((OpData) opsData).getMechTime() * MIN_TO_HOUR));
        System.out.println("______________________________");


        return opsData;

    }

}
