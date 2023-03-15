package ru.wert.normic.entities.ops.opPack;

import lombok.Getter;
import lombok.Setter;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.enums.ENormType;
import ru.wert.normic.enums.EOpType;

/**
 * КРЕПЛЕНИЕ К ПОДДОНУ
 */
@Getter
@Setter
public class OpPackOnPallet extends OpData implements PackingData {

    private double polyWrap = 0.0; //Полипропиленовая лента
    private double pallet = 0.0; //Поддон

    private double cartoon = 0.0;
    private double cartoonAngle = 0.0;
    private double stretchMachineWrap = 0.0;
    private double stretchHandWrap = 0.0;
    private double bubbleWrap = 0.0;
    private double ductTape = 0.0;


    public OpPackOnPallet() {
        super.normType = ENormType.NORM_PACKING;
        super.opType = EOpType.PACK_ON_PALLET;
    }
}
