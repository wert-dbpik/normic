package ru.wert.normic.entities;

import lombok.Getter;
import lombok.Setter;
import ru.wert.normic.enums.ENormType;
import ru.wert.normic.enums.EOpType;

/**
 * КРЕПЛЕНИЕ К ПОДДОНУ
 */
@Getter
@Setter
public class OpMountOnPallet extends OpData implements PackingData{


    private double cartoon = 0.0;
    private double stretchMachineWrap = 0.0;
    private double stretchHandWrap = 0.0;
    private double bubbleWrap = 0.0;
    private double polyWrap = 0.0;
    private double ductTape = 0.0;
    private double pallet = 0.0;


    public OpMountOnPallet() {
        super.normType = ENormType.NORM_PACKING;
        super.opType = EOpType.MOUNT_ON_PALLET;
    }
}
