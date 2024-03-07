package ru.wert.normic.entities.ops.opList;

import lombok.Getter;
import lombok.Setter;
import ru.wert.normic.controllers.listOperations.counters.OpBendingCounter;
import ru.wert.normic.controllers.listOperations.counters.OpCuttingCounter;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.enums.EBendingTool;
import ru.wert.normic.enums.ENormType;
import ru.wert.normic.enums.EOpType;
import ru.wert.normic.interfaces.NormCounter;

/**
 * ГИБКА ЛИСТА НА ЛИСТОГИБЕ
 */
@Getter
@Setter
public class OpBending extends OpData {

    private Integer bends = 1;
    private Integer men = 1;
    private EBendingTool tool;

    public OpBending() {
        super.normType = ENormType.NORM_MECHANICAL;
        super.opType = EOpType.BENDING;
    }

    @Override
    public String toString() {
        return "N гибов = " + bends +
                ", N рабочих = " + men +
                ", оборудование = " + tool.getToolName();
    }
}
