package ru.wert.normic.controllers.locksmith.counters;

import ru.wert.normic.dataBaseEntities.ops.OpData;
import ru.wert.normic.dataBaseEntities.ops.opLocksmith.OpAssmNutMK;
import ru.wert.normic.interfaces.NormCounter;

import static ru.wert.normic.AppStatics.roundTo001;
import static ru.wert.normic.controllers.AbstractOpPlate.SEC_TO_MIN;
import static ru.wert.normic.settings.NormConstants.*;

public class OpAssmNutsMKCounter implements NormCounter {

    public OpData count(OpData data){
        OpAssmNutMK opData = (OpAssmNutMK) data;

        int rivets = opData.getRivets();
        int rivetNuts = opData.getRivetNuts();
        int others = opData.getOthers();

        //######################################################

        double time =  rivets * RIVETS_SPEED * SEC_TO_MIN
                + rivetNuts * RIVET_NUTS_SPEED * SEC_TO_MIN
                + others * OTHERS_SPEED * SEC_TO_MIN;   //мин

        opData.setMechTime(roundTo001(time));
        return opData;

    }
}
