package ru.wert.normic.entities.ops.opWelding;

import lombok.Getter;
import lombok.Setter;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.enums.*;

/**
 * СБОРКА СВАРОЧНОЙ КОНСТРУКЦИИ
 */
@Getter
@Setter
public class OpWeldAssm extends OpData {

    private EPartBigness partBigness = EPartBigness.BIG; //Крупная
    private Integer numOfDetails = 2;

    public OpWeldAssm() {
        super.normType = ENormType.NORM_MECHANICAL;
        super.jobType = EJobType.JOB_WELDING;
        super.opType = EOpType.WELD_ASSM;
    }

    @Override
    public String toString() {
        return "Габаритность сборки = " + partBigness.getName();
    }
}
