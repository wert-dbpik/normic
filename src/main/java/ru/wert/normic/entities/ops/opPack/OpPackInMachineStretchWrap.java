package ru.wert.normic.entities.ops.opPack;

import lombok.Getter;
import lombok.Setter;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.enums.ENormType;
import ru.wert.normic.enums.EOpType;

/**
 * УПАКОВКА НА ПАЛЕТОУПАКОВЩИКЕ
 */
@Getter
@Setter
public class OpPackInMachineStretchWrap extends OpData implements PackingData {

    private int partMin = 10; //Минимальная партия
    private double cartoon = 0.0; //Картон
    private double cartoonAngle = 0.0; //Картонный уголок
    private double ductTape = 0.0; //Скотч

    private double stretchMachineWrap = 0.0;
    private double stretchHandWrap = 0.0;
    private double bubbleWrap = 0.0;
    private double polyWrap = 0.0;
    private double pallet = 0.0;


    public OpPackInMachineStretchWrap() {
        super.normType = ENormType.NORM_PACKING;
        super.opType = EOpType.PACK_IN_MACHINE_STRETCH_WRAP;
    }
}
