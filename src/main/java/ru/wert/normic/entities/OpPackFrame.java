package ru.wert.normic.entities;

import lombok.Getter;
import lombok.Setter;
import ru.wert.normic.enums.EColor;
import ru.wert.normic.enums.ENormType;
import ru.wert.normic.enums.EOpType;
import ru.wert.normic.enums.EPaintingDifficulty;
import ru.wert.normic.utils.IntegerParser;

@Getter
@Setter
public class OpPackFrame extends OpData {

    private int width = 0;
    private int depth = 0;
    private int height = 0;

    private boolean roofWrap = false;
    private boolean sideWrap = false;
    private boolean stretchWrap = false;
    private boolean polyWrap = false;
    private boolean bubbleWrap = false;
    private boolean ductTape = false;

    public OpPackFrame() {
        super.normType = ENormType.NORM_PAINTING;
        super.opType = EOpType.PACK_FRAME;
    }
}
