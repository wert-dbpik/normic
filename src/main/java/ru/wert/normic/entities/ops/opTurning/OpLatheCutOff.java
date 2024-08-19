package ru.wert.normic.entities.ops.opTurning;

import lombok.Getter;
import lombok.Setter;
import ru.wert.normic.entities.db_connection.material.Material;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.enums.EJobType;
import ru.wert.normic.enums.ENormType;
import ru.wert.normic.enums.EOpType;

/**
 * ОТРЕЗАНИЕ ДЕТАЛИ НА ТОКАРНОМ СТАНКЕ
 */
@Getter
@Setter
public class OpLatheCutOff extends OpData {

    private Material material;
    private Boolean cutOffSolid = true; //отрезание детали сплошного сечения
    private Double thickness = 0.0; //количество токарных проходов


    public OpLatheCutOff() {
        super.normType = ENormType.NORM_MECHANICAL;
        super.jobType = EJobType.JOB_MECHANIC;
        super.opType = EOpType.LATHE_CUT_OFF;
    }

    @Override
    public String toString() {
        return "деталь сплошного сечения = " + cutOffSolid +
                ", T толщ.стенки = " + thickness + " мм.";
    }
}
