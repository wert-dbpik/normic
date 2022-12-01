package ru.wert.normik.entities;

import lombok.Getter;
import lombok.Setter;
import ru.wert.normik.enums.EBendingTool;
import ru.wert.normik.enums.ENormType;
import ru.wert.normik.enums.EOpType;

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
