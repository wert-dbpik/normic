package ru.wert.normic.entities.ops.opTurning;

import lombok.Getter;
import lombok.Setter;
import ru.wert.normic.controllers.listOperations.counters.OpCuttingCounter;
import ru.wert.normic.controllers.turning.counters.OpLatheMountDismountCounter;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.enums.ELatheHoldType;
import ru.wert.normic.enums.ENormType;
import ru.wert.normic.enums.EOpType;
import ru.wert.normic.interfaces.NormCounter;

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
        super.opType = EOpType.LATHE_MOUNT_DISMOUNT;
    }

    @Override
    public String toString() {
        return "Тип крепления детали = " + ELatheHoldType.values()[holder].getHolderType();
    }
}
