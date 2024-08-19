package ru.wert.normic.controllers.assembling.countings;

import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.opAssembling.OpLevelingSealer;
import ru.wert.normic.interfaces.NormCounter;

import static ru.wert.normic.AppStatics.roundTo001;
import static ru.wert.normic.controllers.AbstractOpPlate.MM_TO_M;
import static ru.wert.normic.settings.NormConstants.LEVELING_PREPARED_TIME;
import static ru.wert.normic.settings.NormConstants.LEVELING_SPEED;

public class OpLevelingSealerCounter implements NormCounter {

    public OpData count(OpData data){
        OpLevelingSealer opData = (OpLevelingSealer)data;

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

        opData.setCompA(roundTo001(compA));
        opData.setCompB(roundTo001(compB));
        opData.setAssmTime(roundTo001(time));
        return opData;
    }

}
