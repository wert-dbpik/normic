package ru.wert.normic.dataBaseEntities.ops.opLocksmith;

import lombok.Getter;
import lombok.Setter;
import ru.wert.normic.dataBaseEntities.ops.OpData;
import ru.wert.normic.enums.EJobType;
import ru.wert.normic.enums.ENormType;
import ru.wert.normic.enums.EOpType;

/**
 * ОТРУБАНИЕ НА ГЕКЕ
 */
@Getter
@Setter
public class OpChopOff extends OpData {


    private int length = 0;


    public OpChopOff() {
        super.normType = ENormType.NORM_MECHANICAL;
        super.jobType = EJobType.JOB_LOCKSMITH;
        super.opType = EOpType.CHOP_OFF;
    }

    @Override
    public String toString() {
        return "без данных";
    }
}
