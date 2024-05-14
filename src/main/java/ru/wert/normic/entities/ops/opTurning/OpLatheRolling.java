package ru.wert.normic.entities.ops.opTurning;

import lombok.Getter;
import lombok.Setter;
import ru.wert.normic.controllers.listOperations.counters.OpCuttingCounter;
import ru.wert.normic.controllers.turning.counters.OpLatheRollingCounter;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.enums.EJobType;
import ru.wert.normic.enums.ENormType;
import ru.wert.normic.enums.EOpType;
import ru.wert.normic.interfaces.NormCounter;

/**
 * АКАТЫВАНИЕ ПРОФИЛЯ НА ТОКАРНОМ СТАНКЕ
 */
@Getter
@Setter
public class OpLatheRolling extends OpData {

    private Integer diameter = 0; //отрезание детали сплошного сечения
    private Integer length = 0; //количество токарных проходов


    public OpLatheRolling() {
        super.normType = ENormType.NORM_MECHANICAL;
        super.jobType = EJobType.JOB_MECHANIC;
        super.opType = EOpType.LATHE_ROLLING;
    }

    @Override
    public String toString() {
        return "D накат = " + diameter + " мм." +
                ", L накат = " + length + " мм.";
    }
}
