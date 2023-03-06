package ru.wert.normic.entities.ops.opTurning;

import lombok.Getter;
import lombok.Setter;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.enums.ENormType;
import ru.wert.normic.enums.EOpType;

/**
 * НАРЕЗАНИЕ КАНАВКИ НА ТОКАРНОМ СТАНКЕ
 */
@Getter
@Setter
public class LatheCutGroove extends OpData {

    private Double depth = 0.0; //глубина паза


    public LatheCutGroove() {
        super.normType = ENormType.NORM_MECHANICAL;
        super.opType = EOpType.LATHE_CUT_GROOVE;
    }
}
