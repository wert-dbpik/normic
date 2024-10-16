package ru.wert.normic.controllers._forms;

import ru.wert.normic.controllers.AbstractOpPlate;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.interfaces.IOpWithOperations;

import static java.lang.String.format;
import static ru.wert.normic.AppStatics.MAIN_OP_DATA;

public class TotalCounter {

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
                op.setTotal(amount * op.getQuantity());
                if(op.getOpType().getNormCounter() == null) //Плашка ERROR
                    continue;
                op = op.getOpType().getNormCounter().count(op); //Вычисляем, аргуметом передаем саму операцию

                //Если операция отображается на плашке, то
                AbstractOpPlate plate = op.getPlateController();
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

        return opsData;

    }

}
