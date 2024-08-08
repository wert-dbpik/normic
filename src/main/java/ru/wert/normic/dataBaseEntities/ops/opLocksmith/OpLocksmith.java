package ru.wert.normic.dataBaseEntities.ops.opLocksmith;

import lombok.Getter;
import lombok.Setter;
import ru.wert.normic.dataBaseEntities.ops.OpData;
import ru.wert.normic.enums.EJobType;
import ru.wert.normic.enums.ENormType;
import ru.wert.normic.enums.EOpType;

/**
 * СЛЕСАРНЫЕ ОПЕРАЦИИ
 */
@Getter
@Setter
public class OpLocksmith extends OpData {

    private Integer rivets = 0; //Вытяжные заклепки
    private Integer countersinkings = 0; //Зенкования
    private Integer threadings = 0; //Нарезания резьбы

    public OpLocksmith() {
        super.normType = ENormType.NORM_MECHANICAL;
        super.jobType = EJobType.JOB_LOCKSMITH;
        super.opType = EOpType.LOCKSMITH;
    }

    @Override
    public String toString() {
        return "заклепки вытяжные = " + rivets + " шт." +
                ", зенкования = " + countersinkings +
                ", нарезания резьбы = " + threadings;
    }
}
