package ru.wert.normic.entities.ops.opTurning;

import lombok.Getter;
import lombok.Setter;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.enums.EJobType;
import ru.wert.normic.enums.ENormType;
import ru.wert.normic.enums.EOpType;

/**
 * НАРЕЗАНИЕ КАНАВКИ НА ТОКАРНОМ СТАНКЕ
 */
@Getter
@Setter
public class OpLatheCutGroove extends OpData {

    private Double depth = 0.0; //глубина паза


    public OpLatheCutGroove() {
        super.normType = ENormType.NORM_MECHANICAL;
        super.jobType = EJobType.JOB_MECHANIC;
        super.opType = EOpType.LATHE_CUT_GROOVE;
    }

    @Override
    public String toString() {
        return "T глуб.паза = " + depth + " мм.";
    }
}
