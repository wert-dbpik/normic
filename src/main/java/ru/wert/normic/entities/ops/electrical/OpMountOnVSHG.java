package ru.wert.normic.entities.ops.electrical;

import lombok.Getter;
import lombok.Setter;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.enums.ENormType;
import ru.wert.normic.enums.EOpType;

/**
 * УСТАНОВКА НА ВШГ (4 ШТ)
 */
@Getter
@Setter
public class OpMountOnVSHG extends OpData {

    private int elements = 0;
    private boolean difficult = false;


    public OpMountOnVSHG() {
        super.normType = ENormType.NORM_ELECTRICAL;
        super.opType = EOpType.EL_MOUNT_ON_VSHG;
    }

    @Override
    public String toString() {
        return String.format("элементов- %s; Трудность доступа - %s", elements);
    }
}
