package ru.wert.normic.entities.ops.opTurning;

import lombok.Getter;
import lombok.Setter;
import ru.wert.normic.controllers.listOperations.counters.OpCuttingCounter;
import ru.wert.normic.controllers.turning.counters.OpLatheTurningCounter;
import ru.wert.normic.entities.db_connection.material.Material;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.enums.ENormType;
import ru.wert.normic.enums.EOpType;
import ru.wert.normic.interfaces.NormCounter;

/**
 * ТОЧЕНИЕ ИЛИ РАСТАЧИВАНИЕ НА ТОКАРНОМ СТАНКЕ
 */
@Getter
@Setter
public class OpLatheTurning extends OpData {

    private Material material; //материал
    private Integer length = 0; //длина точения
    private Integer passages = 1; //количество токарных проходов


    public OpLatheTurning() {
        super.normType = ENormType.NORM_MECHANICAL;
        super.opType = EOpType.LATHE_TURNING;
    }

    @Override
    public String toString() {
        return "L точения = " + length + " мм." +
                ", T проходов = " + passages;
    }
}
