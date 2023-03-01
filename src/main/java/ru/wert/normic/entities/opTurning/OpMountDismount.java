package ru.wert.normic.entities.opTurning;

import lombok.Getter;
import lombok.Setter;
import ru.wert.normic.entities.OpData;
import ru.wert.normic.enums.ENormType;
import ru.wert.normic.enums.EOpType;

@Getter
@Setter
public class OpMountDismount extends OpData {

    private int holder = 1; //тип крепления детали на станке


    public OpMountDismount() {
        super.normType = ENormType.NORM_MECHANICAL;
        super.opType = EOpType.MOUNT_DISMOUNT;
    }


}
