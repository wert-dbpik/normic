package ru.wert.normic.entities.ops.opList;

import lombok.Getter;
import lombok.Setter;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.enums.EBendingTool;
import ru.wert.normic.enums.ENormType;
import ru.wert.normic.enums.EOpType;

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
}
