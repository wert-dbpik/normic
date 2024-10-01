package ru.wert.normic.controllers.listOperations.counters;

import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.opList.OpBending;
import ru.wert.normic.enums.EBendingTool;
import ru.wert.normic.interfaces.NormCounter;

import static ru.wert.normic.AppStatics.*;
import static ru.wert.normic.controllers.AbstractOpPlate.SEC_TO_MIN;
import static ru.wert.normic.settings.NormConstants.BENDING_SERVICE_RATIO;
import static ru.wert.normic.settings.NormConstants.UNIVERSAL_BENDING_SPEED;

public class OpBendingCounter  implements NormCounter {

    static double PANELEGIB_BENDING_SPEED = 4; //Скорость гибки на панелегибе, сек
    static double PANELEGIB_TURNING_SPEED = 8; //Скорость поворота на панелегибе, сек
    static double PANELEGIB_WORKING_TIME = 1.28; //Вспомогательное время на панелегибе, мин
    static double PANELEGIB_TIME_CHOOSE_SCRIPT = 24; //Время выбора программы на панелегибе, сек
    static double PANELEGIB_SERVICE_TIME = 0.04; //Коэффициент на обслуживания от оперативного времени
    static double PANELEGIB_REST_TIME = 0.07; //Коэффициент на отдых от оперативного времени

    public OpData count(OpData data){
        OpBending opData = (OpBending)data;
        int bends = opData.getBends(); //Количество гибов

        //Переменые для гибки на универсальном станке
        int men = opData.getMen(); //Количество рабочих
        boolean difficult = opData.isDifficult(); //Сложность

        //Переменые для гибки на панелегибе
        int turns = opData.getTurns(); //Количество поворотов для панелегиба

        //######################################################
        double time;
        double operationTime;
        double timePZ;
        if(opData.getTool().equals(EBendingTool.PANELEGIB)) {
            operationTime = bends * PANELEGIB_BENDING_SPEED * SEC_TO_MIN + turns * PANELEGIB_TURNING_SPEED * SEC_TO_MIN + PANELEGIB_WORKING_TIME;
            timePZ = PANELEGIB_TIME_CHOOSE_SCRIPT * SEC_TO_MIN;
            time = operationTime +
                    PANELEGIB_SERVICE_TIME * operationTime + //Время на обслуживание
                    PANELEGIB_REST_TIME * operationTime +  //Время на отдых
                    timePZ / CURRENT_BATCH / opData.getTotal(); //Время общее на партию
        }
        else {
            operationTime = bends * UNIVERSAL_BENDING_SPEED * men  * BENDING_SERVICE_RATIO;
            timePZ = difficult ? 12.0 : 3.0; //Сложные детали - 12минут
            if(!BATCHNESS.get())
                time = operationTime;
            else time = operationTime +
                    timePZ / CURRENT_BATCH / opData.getTotal();
        }

        opData.setMechTime(roundTo001(time));
        return opData;
    }

}
