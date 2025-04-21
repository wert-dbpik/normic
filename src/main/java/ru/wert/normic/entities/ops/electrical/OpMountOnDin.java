package ru.wert.normic.entities.ops.electrical;

import lombok.Getter;
import lombok.Setter;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.enums.EJobType;
import ru.wert.normic.enums.ENormType;
import ru.wert.normic.enums.EOpType;

/**
 * УСТАНОВКА НА ДИНРЕЙКУ
 */
@Getter
@Setter
public class OpMountOnDin extends OpData {

    private int avtomats = 0;
    private int heaters = 0;


    public OpMountOnDin() {
        super.normType = ENormType.NORM_ELECTRICAL;
        super.opType = EOpType.EL_MOUNT_ON_DIN;
    }

    @Override
    public String toString() {
        return String.format("автоматы - %s, нагреватели - %s", avtomats, heaters);
    }
}
