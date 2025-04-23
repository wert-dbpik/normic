package ru.wert.normic.entities.ops.electrical;

import lombok.Getter;
import lombok.Setter;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.enums.ENormType;
import ru.wert.normic.enums.EOpType;

/**
 * УСТАНОВКА НА 4 ВИНТА C РАЗБОРКОЙ КОРПУСА
 */
@Getter
@Setter
public class OpMountOnScrewsWithDisAssm4 extends OpData {

    private String name = "";
    private int fourScrews = 1;
    private boolean difficult = false;


    public OpMountOnScrewsWithDisAssm4() {
        super.normType = ENormType.NORM_ELECTRICAL;
        super.opType = EOpType.EL_MOUNT_ON_SCREWS_WITH_DISASSM_4;
    }

    @Override
    public String toString() {
        return String.format("Примечание - %s; \n; 4 винта - %s; трудность доступа - %s", name, fourScrews, difficult);
    }
}
