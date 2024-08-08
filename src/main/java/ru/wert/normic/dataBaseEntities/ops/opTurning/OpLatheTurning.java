package ru.wert.normic.dataBaseEntities.ops.opTurning;

import lombok.Getter;
import lombok.Setter;
import ru.wert.normic.dataBaseEntities.db_connection.material.Material;
import ru.wert.normic.dataBaseEntities.ops.OpData;
import ru.wert.normic.enums.EJobType;
import ru.wert.normic.enums.ENormType;
import ru.wert.normic.enums.EOpType;

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
        super.jobType = EJobType.JOB_MECHANIC;
        super.opType = EOpType.LATHE_TURNING;
    }

    @Override
    public String toString() {
        return "L точения = " + length + " мм." +
                ", T проходов = " + passages;
    }
}
