package ru.wert.normic.entities.opTurning;

import lombok.Getter;
import lombok.Setter;
import ru.wert.normic.entities.OpData;
import ru.wert.normic.enums.ENormType;
import ru.wert.normic.enums.EOpType;

@Getter
@Setter
public class OpCutOff extends OpData {

    private Boolean cutOffSolid = true; //отрезание детали сплошного сечения
    private Double thickness = 0.0; //количество токарных проходов


    public OpCutOff() {
        super.normType = ENormType.NORM_MECHANICAL;
        super.opType = EOpType.CUT_OFF;
    }
}
