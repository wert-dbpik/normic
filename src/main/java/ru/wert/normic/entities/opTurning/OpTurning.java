package ru.wert.normic.entities.opTurning;

import lombok.Getter;
import lombok.Setter;
import ru.wert.normic.entities.OpData;
import ru.wert.normic.enums.ENormType;
import ru.wert.normic.enums.EOpType;
import ru.wert.normic.enums.EPartBigness;

@Getter
@Setter
public class OpTurning extends OpData {

    private Integer length = 0; //длина точения
    private Integer passages = 1; //количество токарных проходов


    public OpTurning() {
        super.normType = ENormType.NORM_MECHANICAL;
        super.opType = EOpType.TURNING;
    }
}
