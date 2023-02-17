package ru.wert.normic.entities;

import lombok.Getter;
import lombok.Setter;
import ru.wert.normic.enums.ENormType;
import ru.wert.normic.enums.EOpType;

@Getter
@Setter
public class OpRolling extends OpData {

    private Integer diameter = 0; //отрезание детали сплошного сечения
    private Integer length = 0; //количество токарных проходов


    public OpRolling() {
        super.normType = ENormType.NORM_MECHANICAL;
        super.opType = EOpType.ROLLING;
    }
}
