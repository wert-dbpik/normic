package ru.wert.normic.entities.opTurning;

import lombok.Getter;
import lombok.Setter;
import ru.wert.normic.entities.OpData;
import ru.wert.normic.enums.ENormType;
import ru.wert.normic.enums.EOpType;

@Getter
@Setter
public class OpCutGroove extends OpData {

    private Double depth = 0.0; //глубина паза


    public OpCutGroove() {
        super.normType = ENormType.NORM_MECHANICAL;
        super.opType = EOpType.CUT_GROOVE;
    }
}
