package ru.wert.normic.controllers.list;

import ru.wert.normic.entities.db_connection.material.Material;
import ru.wert.normic.entities.ops.opList.OpCutting;

import static ru.wert.normic.controllers.AbstractOpPlate.MM2_TO_M2;
import static ru.wert.normic.controllers.AbstractOpPlate.MM_TO_M;
import static ru.wert.normic.settings.NormConstants.*;

public class PlateCuttingCounter {

    private Material material ;
    private Integer paramA;
    private Integer paramB;
    private Integer holes;
    private Integer perfHoles;
    private Integer extraPerimeter;
    private boolean stripping;

    public static OpCutting count(OpCutting opData){

        double t = opData.getMaterial().getParamS(); //Толщина материала
        int paramA = opData.getParamA(); //Параметр А развертки
        int paramB = opData.getParamB(); //Параметр B развертки
        double perimeter = 2 * (paramA + paramB) * MM_TO_M;; //Периметр контура развертки
        double area = paramA * paramB * MM2_TO_M2; //Площадь развертки
        int extraPerimeter = opData.getExtraPerimeter(); //Дополнительный периметр обработки
        boolean stripping = opData.isStripping(); //Применить зачистку
        int holes = opData.getHoles(); //Количество отверстий в развертке
        int perfHoles = opData.getPerfHoles(); //Количество перфораций в развертке


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

        double time;

        time = ((perimeter + PLUS_LENGTH)/speed                 //Время на резку по периметру
                + CUTTING_SPEED * area                          //Время подготовительное - заключительоне
                + REVOLVER_SPEED * holes                //Время на пробивку отверстий
                + PERFORATION_SPEED * perfHoles)        //Время на пробивку перфорации
                * CUTTING_SERVICE_RATIO
                + strippingTime;

        if(area == 0.0) time = 0.0;

        opData.setMechTime(time);

        return opData;
    }
}
