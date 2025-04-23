package ru.wert.normic.entities.ops.electrical;

import lombok.Getter;
import lombok.Setter;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.enums.ENormType;
import ru.wert.normic.enums.EOpType;

/**
 * ЛУЖЕНИЕ ЭЛЕКТРОПАЯЛЬНИКОМ
 */
@Getter
@Setter
public class OpTinning extends OpData {

    private String name = "";
    private int pins = 1;


    public OpTinning() {
        super.normType = ENormType.NORM_ELECTRICAL;
        super.opType = EOpType.EL_TINNING;
    }

    @Override
    public String toString() {
        return String.format("Наконечников - %s", pins);
    }
}
