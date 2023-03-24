package ru.wert.normic.entities.ops.opLocksmith;

import lombok.Getter;
import lombok.Setter;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.enums.ENormType;
import ru.wert.normic.enums.EOpType;

/**
 * КРЕПЕЖ (УЧАСТОК МК)
 */
@Getter
@Setter
public class OpAssmNutMK extends OpData {

    private Integer rivets = 0; //заклепки
    private Integer rivetNuts = 0; //заклепочные гайки
    private Integer others = 0;//другой крепеж

    public OpAssmNutMK() {
        super.normType = ENormType.NORM_MECHANICAL;
        super.opType = EOpType.ASSM_NUTS_MK;
    }

    @Override
    public String toString() {
        return "заклепки вытяжные = " + rivets + " шт." +
                ", заклепочные гайки = " + rivetNuts + " шт." +
                ", др.элементы = " + others  + " шт.";
    }
}
