package ru.wert.normic.dataBaseEntities.ops.opLocksmith;

import lombok.Getter;
import lombok.Setter;
import ru.wert.normic.dataBaseEntities.ops.OpData;
import ru.wert.normic.enums.EJobType;
import ru.wert.normic.enums.ENormType;
import ru.wert.normic.enums.EOpType;

/**
 * СВЕРЛЕНИЕ ОТВЕРСТИЙ ПО РАЗМЕТКЕ
 */
@Getter
@Setter
public class OpDrillingByMarking extends OpData {

    private Integer diameter = 0; //диаметр отверстий
    private Integer depth = 0; //глубина отверстий
    private Integer holes = 0; //количество отверстий
    private Integer length = 0; //максимальный измеряемый размер или шаг между отверстиями


    public OpDrillingByMarking() {
        super.normType = ENormType.NORM_MECHANICAL;
        super.jobType = EJobType.JOB_LOCKSMITH;
        super.opType = EOpType.DRILLING_BY_MARKING;
    }

    @Override
    public String toString() {
        return "D отв = " + diameter + " мм." +
                ", T гл = " + depth + " мм." +
                ", N отв = " + holes +
                ", L max.дл./шаг = " + length + " мм.";
    }
}
