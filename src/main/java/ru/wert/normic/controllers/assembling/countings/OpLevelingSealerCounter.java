package ru.wert.normic.controllers.assembling.countings;

import ru.wert.normic.entities.ops.opAssembling.OpLevelingSealer;
import ru.wert.normic.enums.ESealersWidth;

import static ru.wert.normic.controllers.AbstractOpPlate.DOUBLE_FORMAT;
import static ru.wert.normic.controllers.AbstractOpPlate.MM_TO_M;
import static ru.wert.normic.settings.NormConstants.LEVELING_PREPARED_TIME;
import static ru.wert.normic.settings.NormConstants.LEVELING_SPEED;

public class OpLevelingSealerCounter {

    public static OpLevelingSealer count(OpLevelingSealer opData){
        String name = opData.getName(); //Наименование
        int paramA = opData.getParamA(); //Размер А
        int paramB = opData.getParamB();//Размер Б
        double perimeter = (paramA == 0 || paramB == 0) ?
                (paramA + paramB)  * MM_TO_M :
                2 * (paramA + paramB) * MM_TO_M;; //

        double normCompA = opData.getSealersWidth().getCompA(); //Компонент А на метр длины
        double normCompB = opData.getSealersWidth().getCompB(); //Компонент Б на метр длины


        //#########################################################################################

        double time =  perimeter * LEVELING_SPEED +
                Math.ceil(perimeter / 6.0) * LEVELING_PREPARED_TIME;  //мин

        double compA = perimeter * normCompA;
        double compB = perimeter * normCompB;

        opData.setCompA(compA);
        opData.setCompB(compB);
        opData.setAssmTime(time);
        return opData;
    }

}
