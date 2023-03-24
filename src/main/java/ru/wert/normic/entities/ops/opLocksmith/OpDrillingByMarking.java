package ru.wert.normic.entities.ops.opLocksmith;

import lombok.Getter;
import lombok.Setter;
import ru.wert.normic.entities.ops.OpData;
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
