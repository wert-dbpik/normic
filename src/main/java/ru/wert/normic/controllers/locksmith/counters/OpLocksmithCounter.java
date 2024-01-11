package ru.wert.normic.controllers.locksmith.counters;

import ru.wert.normic.entities.ops.opLocksmith.OpLocksmith;

import static ru.wert.normic.controllers.AbstractOpPlate.SEC_TO_MIN;
import static ru.wert.normic.settings.NormConstants.*;

public class OpLocksmithCounter {

    public static OpLocksmith count(OpLocksmith opData){

        int rivets = opData.getRivets();
        int countersinkings = opData.getCountersinkings();
        int threadings = opData.getThreadings();

        //######################################################

        double time =  rivets * RIVETS_SPEED  * SEC_TO_MIN
                + countersinkings * COUNTERSINKING_SPEED
                + threadings * THREADING_SPEED;   //мин

        opData.setMechTime(time);
        return opData;
    }
}
