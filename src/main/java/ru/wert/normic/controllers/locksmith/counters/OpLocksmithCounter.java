package ru.wert.normic.controllers.locksmith.counters;

import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.opList.OpCutting;
import ru.wert.normic.entities.ops.opLocksmith.OpLocksmith;
import ru.wert.normic.interfaces.NormCounter;

import static ru.wert.normic.controllers.AbstractOpPlate.SEC_TO_MIN;
import static ru.wert.normic.settings.NormConstants.*;

public class OpLocksmithCounter implements NormCounter {

    public OpData count(OpData data){
        OpLocksmith opData = (OpLocksmith)data;

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
