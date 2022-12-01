package ru.wert.normik.entities;

import lombok.Getter;
import lombok.Setter;
import ru.wert.normik.enums.ENormType;
import ru.wert.normik.enums.EOpType;

@Getter
@Setter
public class OpWeldDotted extends OpData {

    private Integer parts = 0; //Количество элементов
    private Integer dots = 0; //Количество точек
    private Integer drops = 0; //Количество прихваток

    public OpWeldDotted() {
        super.normType = ENormType.NORM_MECHANICAL;
        super.opType = EOpType.WELD_DOTTED;
    }
}
