package ru.wert.normic.entities.ops.electrical;

import lombok.Getter;
import lombok.Setter;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.enums.ENormType;
import ru.wert.normic.enums.EOpType;

/**
 * РЕЗКА КАБЕЛЬКАНАЛА, ДИНРЕЙКИ
 */
@Getter
@Setter
public class OpCutCableChannel extends OpData {

    private String name = "";
    private int cuts = 1;


    public OpCutCableChannel() {
        super.normType = ENormType.NORM_ELECTRICAL;
        super.opType = EOpType.EL_CUT_CABLE_CHANNEL;
    }

    @Override
    public String toString() {
        return String.format("Резов - %s", cuts);
    }
}
