package ru.wert.normic.entities.ops.opTurning;

import lombok.Getter;
import lombok.Setter;
import ru.wert.normic.controllers.listOperations.counters.OpCuttingCounter;
import ru.wert.normic.controllers.turning.counters.OpLatheDrillingCounter;
import ru.wert.normic.entities.db_connection.material.Material;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.enums.ENormType;
import ru.wert.normic.enums.EOpType;
import ru.wert.normic.interfaces.NormCounter;

/**
 * СВЕРЛЕНИЕ ОТВЕРСТИЯ НА ТОКАРНОМ СТАНКЕ
 */
@Getter
@Setter
public class OpLatheDrilling extends OpData {

    private Integer diameter = 0; //диаметр обработки
    private Integer length = 0; //длина обработки


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
