package ru.wert.normic.controllers.locksmith.counters;

import ru.wert.normic.entities.ops.opLocksmith.OpAssmNutMK;

import static ru.wert.normic.controllers.AbstractOpPlate.SEC_TO_MIN;
import static ru.wert.normic.settings.NormConstants.*;

public class OpAssmNutsMKCounter {

    public static OpAssmNutMK count(OpAssmNutMK opData){

        int rivets = opData.getRivets();
        int rivetNuts = opData.getRivetNuts();
        int others = opData.getOthers();

        //######################################################

        double time =  rivets * RIVETS_SPEED * SEC_TO_MIN
                + rivetNuts * RIVET_NUTS_SPEED * SEC_TO_MIN
                + others * OTHERS_SPEED * SEC_TO_MIN;   //мин

        opData.setMechTime(time);
        return opData;

    }
}
