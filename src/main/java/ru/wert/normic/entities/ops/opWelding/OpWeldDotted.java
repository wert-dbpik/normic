package ru.wert.normic.entities.ops.opWelding;

import lombok.Getter;
import lombok.Setter;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.enums.EJobType;
import ru.wert.normic.enums.ENormType;
import ru.wert.normic.enums.EOpType;

/**
 * СВАРКА ТОЧЕЧНАЯ И ПРИХВАТКАМИ
 */
@Getter
@Setter
public class OpWeldDotted extends OpData {

    private Integer parts = 0; //Количество элементов
    private Integer dots = 0; //Количество точек
    private Integer drops = 0; //Количество прихваток

    public OpWeldDotted() {
        super.normType = ENormType.NORM_MECHANICAL;
        super.jobType = EJobType.JOB_WELDING;
        super.opType = EOpType.WELD_DOTTED;
    }

    @Override
    public String toString() {
        return "элементы = " + parts +
                ", точки = " + dots +
                ", прихватки = " + drops;
    }
}
