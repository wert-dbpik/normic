package ru.wert.normic.entities.ops.opLocksmith;

import lombok.Getter;
import lombok.Setter;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.enums.ENormType;
import ru.wert.normic.enums.EOpType;

/**
 * ОТРУБАНИЕ НА ГЕКЕ
 */
@Getter
@Setter
public class OpChopOff extends OpData {


    public OpChopOff() {
        super.normType = ENormType.NORM_MECHANICAL;
        super.opType = EOpType.CHOP_OFF;
    }

    @Override
    public String toString() {
        return "без данных";
    }
}
