package ru.wert.normic.entities.ops.opPack;

import lombok.Getter;
import lombok.Setter;
import ru.wert.normic.controllers.listOperations.counters.OpCuttingCounter;
import ru.wert.normic.controllers.packing.counters.OpPackInMachineStretchWrapCounter;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.enums.ENormType;
import ru.wert.normic.enums.EOpType;
import ru.wert.normic.interfaces.NormCounter;

/**
 * УПАКОВКА НА ПАЛЕТОУПАКОВЩИКЕ В МАШИННУЮ СТРЕЙЧ ПЛЕНКУ
 */
@Getter
@Setter
public class OpPackInMachineStretchWrap extends OpData implements PackingData {

    private transient NormCounter normCounter = new OpPackInMachineStretchWrapCounter();

    private int height = 0;
    private int width = 0;
    private int depth = 0;

    private int partMin = 10; //Минимальная партия
    private double cartoon = 0.0; //Картон
    private double cartoonAngle = 0.0; //Картонный уголок
    private double ductTape = 0.0; //Скотч
    private double stretchMachineWrap = 0.0; //Машинная стрейч пленка

    private double stretchHandWrap = 0.0;
    private double bubbleWrap = 0.0;
    private double polyWrap = 0.0;
    private double pallet = 0.0;


    public OpPackInMachineStretchWrap() {
        super.normType = ENormType.NORM_PACKING;
        super.opType = EOpType.PACK_IN_MACHINE_STRETCH_WRAP;
    }

    @Override
    public String toString() {
        return "минимальная партия = " + partMin + " шт." +
                ", машинная стрейч-пленка = " + stretchMachineWrap + " м." +
                ",\nкартон = " + cartoon + " м.кв." +
                ", картонные уголки = " + cartoonAngle + " м.кв." +
                ", скотч = " + ductTape + " шт.";

   }
}
