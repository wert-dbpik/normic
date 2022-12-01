package ru.wert.normik.entities;

import lombok.Getter;
import lombok.Setter;
import ru.wert.normik.enums.EAssemblingType;
import ru.wert.normik.enums.ENormType;
import ru.wert.normik.enums.EOpType;

@Getter
@Setter
public class OpPaintAssm extends OpData {

    private double area = 0.0;
    private Integer along = 0;
    private Integer across = 0;
    private EAssemblingType assmType = EAssemblingType.SOLID;

    public OpPaintAssm() {
        super.normType = ENormType.NORM_PAINTING;
        super.opType = EOpType.PAINTING_ASSM;
    }
}
