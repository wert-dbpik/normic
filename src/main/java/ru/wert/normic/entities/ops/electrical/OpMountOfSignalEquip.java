package ru.wert.normic.entities.ops.electrical;

import lombok.Getter;
import lombok.Setter;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.enums.ENormType;
import ru.wert.normic.enums.EOpType;

/**
 * УСТАНОВКА СИГНАЛЬНОГО ОБОРУДОВАНИЯ
 */
@Getter
@Setter
public class OpMountOfSignalEquip extends OpData {

    private int elements = 0;


    public OpMountOfSignalEquip() {
        super.normType = ENormType.NORM_ELECTRICAL;
        super.opType = EOpType.EL_MOUNT_OF_SIGNAL_EQUIP;
    }

    @Override
    public String toString() {
        return String.format("элементов- %s", elements);
    }
}
