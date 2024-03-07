package ru.wert.normic.entities.ops.opLocksmith;

import lombok.Getter;
import lombok.Setter;
import ru.wert.normic.controllers.listOperations.counters.OpCuttingCounter;
import ru.wert.normic.controllers.locksmith.counters.OpLocksmithCounter;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.enums.ENormType;
import ru.wert.normic.enums.EOpType;
import ru.wert.normic.interfaces.NormCounter;

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
        super.opType = EOpType.LOCKSMITH;
    }

    @Override
    public String toString() {
        return "заклепки вытяжные = " + rivets + " шт." +
                ", зенкования = " + countersinkings +
                ", нарезания резьбы = " + threadings;
    }
}
