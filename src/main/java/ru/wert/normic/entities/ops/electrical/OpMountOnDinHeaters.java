package ru.wert.normic.entities.ops.electrical;

import lombok.Getter;
import lombok.Setter;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.enums.ENormType;
import ru.wert.normic.enums.EOpType;

/**
 * УСТАНОВКА НА ДИНРЕЙКУ НАГРЕВАТЕЛЕЙ
 */
@Getter
@Setter
public class OpMountOnDinHeaters extends OpData {

    private String name = "";
    private int heaters = 1;
    private boolean difficult = false;


    public OpMountOnDinHeaters() {
        super.normType = ENormType.NORM_ELECTRICAL;
        super.opType = EOpType.EL_MOUNT_ON_DIN_HEATERS;
    }

    @Override
    public String toString() {
        return String.format("Примечание - %s; \nнагреватели - %s; Трудность доступа - %s", name, heaters, difficult);
    }
}
