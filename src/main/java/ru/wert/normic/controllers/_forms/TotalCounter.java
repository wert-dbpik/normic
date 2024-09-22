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
