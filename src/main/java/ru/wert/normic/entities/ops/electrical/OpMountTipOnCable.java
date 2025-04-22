package ru.wert.normic.entities.ops.electrical;

import lombok.Getter;
import lombok.Setter;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.enums.ENormType;
import ru.wert.normic.enums.EOpType;

/**
 * ОКОНЦОВКА ПРОВОДА
 */
@Getter
@Setter
public class OpMountTipOnCable extends OpData {

    private int tips = 1;


    public OpMountTipOnCable() {
        super.normType = ENormType.NORM_ELECTRICAL;
        super.opType = EOpType.EL_MOUNT_TIP_ON_CABLE;
    }

    @Override
    public String toString() {
        return String.format("Наконечников - %s", tips);
    }
}
