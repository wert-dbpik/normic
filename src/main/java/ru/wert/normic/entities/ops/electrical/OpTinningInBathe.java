package ru.wert.normic.entities.ops.electrical;

import lombok.Getter;
import lombok.Setter;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.enums.ENormType;
import ru.wert.normic.enums.EOpType;

/**
 * ЛУЖЕНИЕ В ВАННОЧКЕ
 */
@Getter
@Setter
public class OpTinningInBathe extends OpData {

    private int pins = 0;


    public OpTinningInBathe() {
        super.normType = ENormType.NORM_ELECTRICAL;
        super.opType = EOpType.EL_TINNING_IN_BATHE;
    }

    @Override
    public String toString() {
        return String.format("Наконечников - %s", pins);
    }
}
