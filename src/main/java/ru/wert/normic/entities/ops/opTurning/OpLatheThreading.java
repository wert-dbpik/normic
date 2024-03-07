package ru.wert.normic.entities.ops.opTurning;

import lombok.Getter;
import lombok.Setter;
import ru.wert.normic.controllers.listOperations.counters.OpCuttingCounter;
import ru.wert.normic.controllers.turning.counters.OpLatheThreadingCounter;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.enums.ENormType;
import ru.wert.normic.enums.EOpType;
import ru.wert.normic.interfaces.NormCounter;

/**
 * НАРЕЗАНИЕ РЕЗЬБЫ НА ТОКАРНОМ СТАНКЕ
 */
@Getter
@Setter
public class OpLatheThreading extends OpData {

    private Integer diameter = 0; //отрезание детали сплошного сечения
    private Integer length = 0; //количество токарных проходов


    public OpLatheThreading() {
        super.normType = ENormType.NORM_MECHANICAL;
        super.opType = EOpType.LATHE_THREADING;
    }

    @Override
    public String toString() {
        return "D резьбы = " + diameter + " мм." +
                ", L резьбы = " + length + " мм.";
    }
}
