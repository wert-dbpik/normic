package ru.wert.normic.dataBaseEntities.ops.opTurning;

import lombok.Getter;
import lombok.Setter;
import ru.wert.normic.dataBaseEntities.ops.OpData;
import ru.wert.normic.enums.EJobType;
import ru.wert.normic.enums.ENormType;
import ru.wert.normic.enums.EOpType;

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
        super.jobType = EJobType.JOB_MECHANIC;
        super.opType = EOpType.LATHE_THREADING;
    }

    @Override
    public String toString() {
        return "D резьбы = " + diameter + " мм." +
                ", L резьбы = " + length + " мм.";
    }
}
