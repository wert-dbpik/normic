package ru.wert.normic.entities.ops.electrical;

import lombok.Getter;
import lombok.Setter;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.enums.ENormType;
import ru.wert.normic.enums.EOpType;

/**
 * УСТАНОВКА КАБЕЛЬНЫХ ВВОДОВ
 */
@Getter
@Setter
public class OpMountOfCableEntries extends OpData {

    private int elements = 0;


    public OpMountOfCableEntries() {
        super.normType = ENormType.NORM_ELECTRICAL;
        super.opType = EOpType.EL_MOUNT_OF_CABLE_ENTRIES;
    }

    @Override
    public String toString() {
        return String.format("элементов- %s", elements);
    }
}
