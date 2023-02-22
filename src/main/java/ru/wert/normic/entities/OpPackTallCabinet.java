package ru.wert.normic.entities;

import lombok.Getter;
import lombok.Setter;
import ru.wert.normic.enums.ENormType;
import ru.wert.normic.enums.EOpType;

/**
 * УПАКОВКА ВЫСОКОГО ШКАФА
 */
@Getter
@Setter
public class OpPackTallCabinet extends OpData  implements PackingData{

    private double cartoon = 0.0;
    private double stretchMachineWrap = 0.0;
    private double stretchHandWrap = 0.0;
    private double bubbleWrap = 0.0;
    private double polyWrap = 0.0;
    private double ductTape = 0.0;
    private double pallet = 0.0;

    private int width = 0;
    private int depth = 0;
    private int height = 0;

    private boolean useRoofWrap = false;
    private boolean useSideWrap = false;
    private boolean useStretchWrap = false;
    private boolean usePolyWrap = false;
    private boolean useBubbleWrap = false;
    private boolean useDuctTape = false;

    public OpPackTallCabinet() {
        super.normType = ENormType.NORM_PACKING;
        super.opType = EOpType.PACK_CABINET;
    }
}
