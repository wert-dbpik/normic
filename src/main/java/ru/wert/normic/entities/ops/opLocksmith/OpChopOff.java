package ru.wert.normic.entities.ops.opLocksmith;

import lombok.Getter;
import lombok.Setter;
import ru.wert.normic.controllers.listOperations.counters.OpCuttingCounter;
import ru.wert.normic.controllers.locksmith.counters.OpChopOffCounter;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.enums.ENormType;
import ru.wert.normic.enums.EOpType;
import ru.wert.normic.interfaces.NormCounter;

/**
 * ОТРУБАНИЕ НА ГЕКЕ
 */
@Getter
@Setter
public class OpChopOff extends OpData {

    private transient NormCounter normCounter = new OpChopOffCounter();

    private int length = 0;


    public OpChopOff() {
        super.normType = ENormType.NORM_MECHANICAL;
        super.opType = EOpType.CHOP_OFF;
    }

    @Override
    public String toString() {
        return "без данных";
    }
}
