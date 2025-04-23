package ru.wert.normic.entities.ops.electrical;

import lombok.Getter;
import lombok.Setter;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.enums.ENormType;
import ru.wert.normic.enums.EOpType;

/**
 * ОКОНЦОВКА СИЛОВОГО КАБЕЛЯ
 */
@Getter
@Setter
public class OpMountTipOnPowerCable extends OpData {

    private String name = "";
    private int tips = 1;


    public OpMountTipOnPowerCable() {
        super.normType = ENormType.NORM_ELECTRICAL;
        super.opType = EOpType.EL_MOUNT_TIP_ON_POWER_CABLE;
    }

    @Override
    public String toString() {
        return String.format("Наконечников - %s", tips);
    }
}
