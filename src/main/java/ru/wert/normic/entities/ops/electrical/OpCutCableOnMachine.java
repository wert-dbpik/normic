package ru.wert.normic.entities.ops.electrical;

import lombok.Getter;
import lombok.Setter;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.enums.ENormType;
import ru.wert.normic.enums.EOpType;

/**
 * РЕЗКА КАБЕЛЯ НА АВТОМАТЕ
 */
@Getter
@Setter
public class OpCutCableOnMachine extends OpData {

    private int cuts = 1;


    public OpCutCableOnMachine() {
        super.normType = ENormType.NORM_ELECTRICAL;
        super.opType = EOpType.EL_CUT_CABLE_ON_MACHINE;
    }

    @Override
    public String toString() {
        return String.format("Резов - %s", cuts);
    }
}
