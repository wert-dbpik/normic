package ru.wert.normic.entities.ops.electrical;

import lombok.Getter;
import lombok.Setter;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.enums.ENormType;
import ru.wert.normic.enums.EOpType;

/**
 * РЕЗКА КАБЕЛЯ И СНЯТИЕ ИЗОЛЯЦИИ ВРУЧНУЮ
 */
@Getter
@Setter
public class OpCutCableHandly extends OpData {

    private String name = "";
    private int multiCoreCable6mm = 0; //Многожильный провод Дн=6 мм
    private int multiCoreCable11mm = 0; //Многожильный провод Дн=11..15 мм
    private int singleCoreCable = 0; //Одножильный провод
    private boolean difficult = false; // Резка проводов по месту, К = 1,2


    public OpCutCableHandly() {
        super.normType = ENormType.NORM_ELECTRICAL;
        super.opType = EOpType.EL_CUT_CABLE_HANDLY;
    }

    @Override
    public String toString() {
        return String.format("Многожильный провод Дн=6 мм - %s; Многожильный провод Дн=11..15 мм - %s; \nОдножильный провод - %s; Резка проводов по месту - %s",
                multiCoreCable6mm, multiCoreCable11mm, singleCoreCable, difficult);
    }
}
