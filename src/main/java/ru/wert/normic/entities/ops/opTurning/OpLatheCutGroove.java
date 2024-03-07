package ru.wert.normic.entities.ops.opTurning;

import lombok.Getter;
import lombok.Setter;
import ru.wert.normic.controllers.listOperations.counters.OpCuttingCounter;
import ru.wert.normic.controllers.turning.counters.OpLatheCutGrooveCounter;
import ru.wert.normic.entities.db_connection.material.Material;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.enums.ENormType;
import ru.wert.normic.enums.EOpType;
import ru.wert.normic.interfaces.NormCounter;

/**
 * НАРЕЗАНИЕ КАНАВКИ НА ТОКАРНОМ СТАНКЕ
 */
@Getter
@Setter
public class OpLatheCutGroove extends OpData {

    private Double depth = 0.0; //глубина паза


    public OpLatheCutGroove() {
        super.normType = ENormType.NORM_MECHANICAL;
        super.opType = EOpType.LATHE_CUT_GROOVE;
    }

    @Override
    public String toString() {
        return "T глуб.паза = " + depth + " мм.";
    }
}
