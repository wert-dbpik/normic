package ru.wert.normic.entities.ops.electrical;

import lombok.Getter;
import lombok.Setter;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.enums.ENormType;
import ru.wert.normic.enums.EOpType;

/**
 * МАРКИРОВКА
 */
@Getter
@Setter
public class OpMarking extends OpData {

    private int elements = 0;


    public OpMarking() {
        super.normType = ENormType.NORM_ELECTRICAL;
        super.opType = EOpType.EL_MARKING;
    }

    @Override
    public String toString() {
        return String.format("элементов- %s", elements);
    }
}
