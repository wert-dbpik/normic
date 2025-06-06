package ru.wert.normic.entities.ops.electrical;

import lombok.Getter;
import lombok.Setter;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.enums.ENormType;
import ru.wert.normic.enums.EOpType;

/**
 * УСТАНОВКА НА ДИНРЕЙКУ АВТОМАТОВ
 */
@Getter
@Setter
public class OpMountOnDinAutomats extends OpData {

    private String name = "";
    private int avtomats = 1;
    private boolean difficult = false;


    public OpMountOnDinAutomats() {
        super.normType = ENormType.NORM_ELECTRICAL;
        super.opType = EOpType.EL_MOUNT_ON_DIN_AUTOMATS;
    }

    @Override
    public String toString() {
        return String.format("Примечание - %s; \nавтоматы - %s; трудность доступа - %s", name, avtomats, difficult);
    }
}
