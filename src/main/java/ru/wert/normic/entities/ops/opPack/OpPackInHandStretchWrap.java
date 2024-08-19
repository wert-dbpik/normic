package ru.wert.normic.entities.ops.opPack;

import lombok.Getter;
import lombok.Setter;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.enums.ENormType;
import ru.wert.normic.enums.EOpType;

/**
 * УПАКОВКА В РУЧНУЮ СТРЕЙЧ-ПЛЕНКУ
 */
@Getter
@Setter
public class OpPackInHandStretchWrap extends OpData implements PackingData {

    private int height = 0;
    private int width = 0;
    private int depth = 0;

    private int selectedRadioButton = 0;
    private double stretchHandWrap = 0.0; //Ручная стрейч-пленка
    private double ductTape = 0.0; //Скотч

    private double cartoonAngle = 0.0;
    private double stretchMachineWrap = 0.0;
    private double bubbleWrap = 0.0; //Пузырьковая пленка
    private double cartoon = 0.0;
    private double polyWrap = 0.0;
    private double pallet = 0.0;


    public OpPackInHandStretchWrap() {
        super.normType = ENormType.NORM_PACKING;
        super.opType = EOpType.PACK_IN_HAND_STRETCH_WRAP;
    }

    @Override
    public String toString() {
        return "ручная стрейч-пленка = " + stretchHandWrap + " м." +
                ", скотч = " + ductTape + " шт.";
    }
}
