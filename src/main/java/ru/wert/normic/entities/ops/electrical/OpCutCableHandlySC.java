package ru.wert.normic.entities.ops.electrical;

import lombok.Getter;
import lombok.Setter;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.enums.ENormType;
import ru.wert.normic.enums.EOpType;

/**
 * РЕЗКА КАБЕЛЯ И СНЯТИЕ ИЗОЛЯЦИИ ВРУЧНУЮ ОДНОЖИЛЬНЫЙ
 */
@Getter
@Setter
public class OpCutCableHandlySC extends OpData {

    private String name = "";
    private int singleCoreCable = 0; //Одножильный провод
    private boolean difficult = false; // Резка проводов по месту, К = 1,2


    public OpCutCableHandlySC() {
        super.normType = ENormType.NORM_ELECTRICAL;
        super.opType = EOpType.EL_CUT_CABLE_HANDLY_SC;
    }

    @Override
    public String toString() {
        return String.format("Одножильный провод - %s; резка проводов по месту - %s",
                singleCoreCable, difficult);
    }
}
