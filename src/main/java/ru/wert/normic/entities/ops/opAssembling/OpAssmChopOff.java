package ru.wert.normic.entities.ops.opAssembling;

import lombok.Getter;
import lombok.Setter;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.enums.EJobType;
import ru.wert.normic.enums.ENormType;
import ru.wert.normic.enums.EOpType;

/**
 * ОТРУБАНИЕ НА ГЕКЕ
 */
@Getter
@Setter
public class OpAssmChopOff extends OpData {


    private int length = 0;


    public OpAssmChopOff() {
        super.normType = ENormType.NORM_ASSEMBLING;
        super.opType = EOpType.ASSM_CHOP_OFF;
    }

    @Override
    public String toString() {
        return "без данных";
    }
}
