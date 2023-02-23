package ru.wert.normic.entities;

import lombok.Getter;
import lombok.Setter;
import ru.wert.normic.enums.ENormType;
import ru.wert.normic.enums.EOpType;

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
}