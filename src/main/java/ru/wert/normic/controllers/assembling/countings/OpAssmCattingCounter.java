package ru.wert.normic.controllers.assembling.countings;

import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.opAssembling.OpAssmCutting;
import ru.wert.normic.interfaces.NormCounter;

import static ru.wert.normic.AppStatics.roundTo001;
import static ru.wert.normic.controllers.AbstractOpPlate.SEC_TO_MIN;
import static ru.wert.normic.settings.NormConstants.*;

public class OpAssmCattingCounter implements NormCounter {

    public OpData count(OpData data){
        OpAssmCutting opData = (OpAssmCutting)data;

        double sealer = opData.getSealer(); //Уплотнитель на ребро корпуса
        double selfAdhSealer = opData.getSelfAdhSealer(); //Уплотнитель самоклеющийся
        double insulation = opData.getInsulation(); //Утеплитель

        //###########################################################


        double time =  sealer * SEALER_SPEED * SEC_TO_MIN
                + selfAdhSealer * SELF_ADH_SEALER_SPEED * SEC_TO_MIN
                + insulation * INSULATION_SPEED;//мин

        opData.setAssmTime(roundTo001(time));
        return opData;
    }

}
