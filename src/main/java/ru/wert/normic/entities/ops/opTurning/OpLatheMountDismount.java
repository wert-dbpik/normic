package ru.wert.normic.entities.ops.opTurning;

import lombok.Getter;
import lombok.Setter;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.enums.ENormType;
import ru.wert.normic.enums.EOpType;

/**
 * УСТАНОВКА И СНЯТИЕ ДЕТАЛИ С ТОКАРНОГО СТАНКА
 */
@Getter
@Setter
public class OpLatheMountDismount extends OpData {

    private int holder = 1; //тип крепления детали на станке


    public OpLatheMountDismount() {
        super.normType = ENormType.NORM_MECHANICAL;
        super.opType = EOpType.LATHE_MOUNT_DISMOUNT;
    }


}
