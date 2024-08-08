package ru.wert.normic.dataBaseEntities.ops.opLocksmith;

import lombok.Getter;
import lombok.Setter;
import ru.wert.normic.dataBaseEntities.ops.OpData;
import ru.wert.normic.enums.EJobType;
import ru.wert.normic.enums.ENormType;
import ru.wert.normic.enums.EOpType;
import ru.wert.normic.enums.ESawType;

/**
 * ОТРЕЗАНИЕ ЗАГОТОВКИ НА ПИЛЕ
 */
@Getter
@Setter
public class OpCutOffOnTheSaw extends OpData {

    private int length = 0;
    private ESawType saw = ESawType.SMALL_SAW; //отрезание детали сплошного сечения

    public OpCutOffOnTheSaw() {
        super.normType = ENormType.NORM_MECHANICAL;
        super.jobType = EJobType.JOB_LOCKSMITH;
        super.opType = EOpType.CUT_OFF_ON_SAW;
    }

    @Override
    public String toString() {
        return "тип пилы = " + saw.getName();
    }
}
