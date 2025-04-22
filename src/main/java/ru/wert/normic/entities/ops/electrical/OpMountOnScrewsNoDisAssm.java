package ru.wert.normic.entities.ops.electrical;

import lombok.Getter;
import lombok.Setter;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.enums.EJobType;
import ru.wert.normic.enums.ENormType;
import ru.wert.normic.enums.EOpType;

/**
 * УСТАНОВКА НА ВИНТЫ БЕЗ РАЗБОРКИ КОРПУСА
 */
@Getter
@Setter
public class OpMountOnScrewsNoDisAssm extends OpData {

    private int twoScrews = 0;
    private int fourScrews = 0;
    private boolean difficult = false;


    public OpMountOnScrewsNoDisAssm() {
        super.normType = ENormType.NORM_ELECTRICAL;
        super.opType = EOpType.EL_MOUNT_ON_SCREWS_NO_DISASSM;
    }

    @Override
    public String toString() {
        return String.format("2 винта - %s; 4 винта - %s; Трудность доступа - %s", twoScrews, fourScrews);
    }
}
