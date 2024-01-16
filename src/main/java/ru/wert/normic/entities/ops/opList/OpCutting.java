package ru.wert.normic.entities.ops.opList;

import lombok.Getter;
import lombok.Setter;
import ru.wert.normic.controllers.listOperations.counters.OpCuttingCounter;
import ru.wert.normic.entities.db_connection.material.Material;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.enums.ENormType;
import ru.wert.normic.enums.EOpType;
import ru.wert.normic.interfaces.NormCounter;

/**
 * ПОЛУЧЕНИЕ ДЕТАЛИ НА ЛАЗЕРНОМ СТАНКЕ С КРП
 */
@Getter
@Setter
public class OpCutting extends OpData {

    private transient NormCounter normCounter = new OpCuttingCounter();

    private Material material;
    private Integer paramA = 0;
    private Integer paramB = 0;
    private Integer holes = 0;
    private Integer perfHoles = 0;
    private Integer extraPerimeter = 0;
    private boolean stripping = true; //Зачистка


    public OpCutting() {
        super.normType = ENormType.NORM_MECHANICAL;
        super.opType = EOpType.CUTTING;
    }

    @Override
    public String toString() {
        return "A = " + paramA +
                ", B = " + paramB +
                " N отв = " + holes +
                ", N перф = " + perfHoles +
                ", P экстра = " + extraPerimeter + " мм." +
                ", зачистка = " + stripping;
    }
}
