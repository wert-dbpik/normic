package ru.wert.normik.entities;

import lombok.Getter;
import lombok.Setter;
import ru.wert.normik.enums.ENormType;
import ru.wert.normik.enums.EOpType;
import ru.wert.normik.enums.EPaintingDifficulty;

@Getter
@Setter
public class OpPaint extends OpData {

    private Integer along = 0;
    private Integer across = 0;
    private EPaintingDifficulty difficulty = EPaintingDifficulty.SIMPLE;
    private Integer hangingTime = 20;

    public OpPaint() {
        super.normType = ENormType.NORM_PAINTING;
        super.opType = EOpType.PAINTING;
    }
}
