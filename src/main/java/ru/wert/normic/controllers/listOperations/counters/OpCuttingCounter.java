package ru.wert.normic.controllers.listOperations.counters;

import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.opList.OpCutting;
import ru.wert.normic.interfaces.NormCounter;

import static ru.wert.normic.AppStatics.*;
import static ru.wert.normic.controllers.AbstractOpPlate.MM2_TO_M2;
import static ru.wert.normic.controllers.AbstractOpPlate.MM_TO_M;
import static ru.wert.normic.settings.NormConstants.*;

public class OpCuttingCounter implements NormCounter {

    public OpData count(OpData data){
        OpCutting opData = (OpCutting)data;

        double t = opData.getMaterial().getParamS(); //Толщина материала
        int paramA = opData.getParamA(); //Параметр А развертки
        int paramB = opData.getParamB(); //Параметр B развертки
        double perimeter = 2 * (paramA + paramB) * MM_TO_M;; //Периметр контура развертки
        double area = paramA * paramB * MM2_TO_M2; //Площадь развертки
        int extraPerimeter = opData.getExtraPerimeter(); //Дополнительный периметр обработки
        boolean stripping = opData.isStripping(); //Применить зачистку
        int holes = opData.getHoles(); //Количество отверстий в развертке
        int perfHoles = opData.getPerfHoles(); //Количество перфораций в развертке

        //##########################################################################

        final double PLUS_LENGTH = extraPerimeter * MM_TO_M;

        double speed;
        //Скорость резания, м/мин
        if (t < 1.5) speed = 5.5;
        else if (t >= 1.5 && t < 2) speed = 5.0;
        else if (t >= 2 && t < 2.5 ) speed = 4.0;
        else if (t >= 2.5 && t < 3.0) speed = 3.0;
        else speed = 1.9;

        //Время зачистки
        double strippingTime; //мин
        if(stripping){
            strippingTime = ((perimeter + PLUS_LENGTH) * STRIPING_SPEED + holes) / 60;
        } else
            strippingTime = 0.0;

        double operationTime =
                ((perimeter + PLUS_LENGTH)/speed                 //Время на резку по периметру
                + CUTTING_SPEED * area                          //Время подготовительное - заключительоне
                + REVOLVER_SPEED * holes                //Время на пробивку отверстий
                + PERFORATION_SPEED * perfHoles)        //Время на пробивку перфорации
                * CUTTING_SERVICE_RATIO;


        double time = operationTime + strippingTime;
        if(BATCHNESS) time = time + 0.25 * operationTime / CURRENT_BATCH;



        if(area == 0.0) time = 0.0;

        opData.setMechTime(roundTo001(time));

        return opData;
    }
}
