package ru.wert.normic.entities;

import lombok.Getter;
import lombok.Setter;
import ru.wert.normic.enums.ENormType;
import ru.wert.normic.enums.EOpType;

@Getter
@Setter
public class OpChopOff extends OpData {


    public OpChopOff() {
        super.normType = ENormType.NORM_MECHANICAL;
        super.opType = EOpType.CHOP_OFF;
    }
}
