package ru.wert.normic.entities.ops.electrical;

import lombok.Getter;
import lombok.Setter;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.enums.ENormType;
import ru.wert.normic.enums.EOpType;

/**
 * РЕЗКА КАБЕЛЯ И СНЯТИЕ ИЗОЛЯЦИИ ВРУЧНУЮ МНОГОЖИЛЬНЫЙ 6 ММ
 */
@Getter
@Setter
public class OpCutCableHandlyMC6 extends OpData {

    private String name = "";
    private int multiCoreCable6mm = 0; //Многожильный провод Дн=6 мм
    private boolean difficult = false; // Резка проводов по месту, К = 1,2


    public OpCutCableHandlyMC6() {
        super.normType = ENormType.NORM_ELECTRICAL;
        super.opType = EOpType.EL_CUT_CABLE_HANDLY_MC6;
    }

    @Override
    public String toString() {
        return String.format("Многожильный провод Дн=6 мм - %s; резка проводов по месту - %s",
                multiCoreCable6mm, difficult);
    }
}
