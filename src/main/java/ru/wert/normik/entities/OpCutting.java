package ru.wert.normik.entities;

import lombok.Getter;
import lombok.Setter;
import ru.wert.normik.enums.ENormType;
import ru.wert.normik.enums.EOpType;

@Getter
@Setter
public class OpCutting extends OpData {

    private Integer holes = 0;
    private Integer perfHoles = 0;
    private Integer extraPerimeter = 0;
    private boolean stripping = false; //Зачистка

    public OpCutting() {
        super.normType = ENormType.NORM_MECHANICAL;
        super.opType = EOpType.CUTTING;
    }
}
