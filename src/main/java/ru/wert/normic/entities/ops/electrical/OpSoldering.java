package ru.wert.normic.entities.ops.electrical;

import lombok.Getter;
import lombok.Setter;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.enums.ENormType;
import ru.wert.normic.enums.EOpType;

/**
 * ОЕДИНЕНИЕ ЭЛЕМЕНТОВ ПАЙКОЙ ЭЛЕКТРОПАЯЛЬНИКОМ
 */
@Getter
@Setter
public class OpSoldering extends OpData {

    private String name = "";
    private int elements = 1;


    public OpSoldering() {
        super.normType = ENormType.NORM_ELECTRICAL;
        super.opType = EOpType.EL_SOLDERING;
    }

    @Override
    public String toString() {
        return String.format("элементов- %s", elements);
    }
}
