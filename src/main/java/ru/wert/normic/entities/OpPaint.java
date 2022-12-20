package ru.wert.normic.entities;

import lombok.Getter;
import lombok.Setter;
import ru.wert.normic.enums.EColor;
import ru.wert.normic.enums.ENormType;
import ru.wert.normic.enums.EOpType;
import ru.wert.normic.enums.EPaintingDifficulty;

@Getter
@Setter
public class OpPaint extends OpData {

    private EColor color = EColor.COLOR_I;
    private boolean twoSides = true;
    private double dyeWeight = 0.0;
    private Integer along = 0;
    private Integer across = 0;
    private EPaintingDifficulty difficulty = EPaintingDifficulty.SIMPLE;
    private Integer hangingTime = 20;

    public OpPaint() {
        super.normType = ENormType.NORM_PAINTING;
        super.opType = EOpType.PAINTING;
    }
}
