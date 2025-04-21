package ru.wert.normic.entities.ops.electrical;

import lombok.Getter;
import lombok.Setter;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.enums.ENormType;
import ru.wert.normic.enums.EOpType;

/**
 * УСТАНОВКА НА ДИНРЕЙКУ
 */
@Getter
@Setter
public class OpSoldering extends OpData {

    private int elements = 0;


    public OpSoldering() {
        super.normType = ENormType.NORM_ELECTRICAL;
        super.opType = EOpType.EL_SOLDERING;
    }

    @Override
    public String toString() {
        return String.format("элементов- %s", elements);
    }
}
