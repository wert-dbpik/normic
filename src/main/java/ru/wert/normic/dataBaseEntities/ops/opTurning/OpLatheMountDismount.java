package ru.wert.normic.dataBaseEntities.ops.opTurning;

import lombok.Getter;
import lombok.Setter;
import ru.wert.normic.dataBaseEntities.ops.OpData;
import ru.wert.normic.enums.EJobType;
import ru.wert.normic.enums.ELatheHoldType;
import ru.wert.normic.enums.ENormType;
import ru.wert.normic.enums.EOpType;

/**
 * УСТАНОВКА И СНЯТИЕ ДЕТАЛИ С ТОКАРНОГО СТАНКА
 */
@Getter
@Setter
public class OpLatheMountDismount extends OpData {

    private double weight = 0.0; //Вес заготовки
    private int holder = 1; //тип крепления детали на станке


    public OpLatheMountDismount() {
        super.normType = ENormType.NORM_MECHANICAL;
        super.jobType = EJobType.JOB_MECHANIC;
        super.opType = EOpType.LATHE_MOUNT_DISMOUNT;
    }

    @Override
    public String toString() {
        return "Тип крепления детали = " + ELatheHoldType.values()[holder].getHolderType();
    }
}
