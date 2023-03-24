package ru.wert.normic.entities.ops.opList;

import lombok.Getter;
import lombok.Setter;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.enums.ENormType;
import ru.wert.normic.enums.EOpType;

/**
 * ПОЛУЧЕНИЕ ДЕТАЛИ НА ЛАЗЕРНОМ СТАНКЕ С КРП
 */
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

    @Override
    public String toString() {
        return " N отв = " + holes +
                ", N перф = " + perfHoles +
                ", P экстра = " + extraPerimeter + " мм." +
                ", зачистка = " + stripping;
    }
}
