package ru.wert.normic.entities.ops.opList;

import lombok.Getter;
import lombok.Setter;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.enums.EBendingTool;
import ru.wert.normic.enums.EJobType;
import ru.wert.normic.enums.ENormType;
import ru.wert.normic.enums.EOpType;

/**
 * ГИБКА ЛИСТА НА ЛИСТОГИБЕ
 */
@Getter
@Setter
public class OpBending extends OpData {

    private Integer bends = 1;
    private Integer turns = 0;
    private Integer men = 1;
    private EBendingTool tool;
    private boolean difficult = false;

    public OpBending() {
        super.normType = ENormType.NORM_MECHANICAL;
        super.jobType = EJobType.JOB_BENDING;
        super.opType = EOpType.BENDING;
    }

    @Override
    public String toString() {
        return "Оборудование = " + tool.getToolName() +
                ", N гибов = " + bends +
                (tool.equals(EBendingTool.UNIVERSAL) ?
                ", N рабочих = " + men + (difficult ? ", сложная" : ", не сложная") :
                ", N поворотов = " + turns);


    }
}
