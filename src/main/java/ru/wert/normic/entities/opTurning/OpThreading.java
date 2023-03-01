package ru.wert.normic.entities.opTurning;

import lombok.Getter;
import lombok.Setter;
import ru.wert.normic.entities.OpData;
import ru.wert.normic.enums.ENormType;
import ru.wert.normic.enums.EOpType;

@Getter
@Setter
public class OpThreading extends OpData {

    private Integer diameter = 0; //отрезание детали сплошного сечения
    private Integer length = 0; //количество токарных проходов


    public OpThreading() {
        super.normType = ENormType.NORM_MECHANICAL;
        super.opType = EOpType.THREADING;
    }
}
