package ru.wert.normic.entities.ops.opTurning;

import lombok.Getter;
import lombok.Setter;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.enums.ENormType;
import ru.wert.normic.enums.EOpType;

/**
 * СВЕРЛЕНИЕ ОТВЕРСТИЯ НА ТОКАРНОМ СТАНКЕ
 */
@Getter
@Setter
public class OpLatheDrilling extends OpData {

    private Integer diameter = 0; //отрезание детали сплошного сечения
    private Integer length = 0; //количество токарных проходов


    public OpLatheDrilling() {
        super.normType = ENormType.NORM_MECHANICAL;
        super.opType = EOpType.LATHE_DRILLING;
    }

    @Override
    public String toString() {
        return "D отв = " + diameter + " мм." +
                ", L отв = " + length + " мм.";
    }
}
