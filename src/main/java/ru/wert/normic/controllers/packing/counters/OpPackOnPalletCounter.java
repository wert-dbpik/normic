package ru.wert.normic.controllers.packing.counters;

import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.opPack.OpPackOnPallet;
import ru.wert.normic.interfaces.NormCounter;

import static ru.wert.normic.AppStatics.roundTo001;
import static ru.wert.normic.controllers.AbstractOpPlate.MM_TO_M;

public class OpPackOnPalletCounter  implements NormCounter {

    private static double palletDepth = 0.800; //габарит квадратного поддона
    private static double palletWidth = 1.200; //габарит квадратного поддона

    public OpData count(OpData data){
        OpPackOnPallet opData = (OpPackOnPallet)data;

        int height = opData.getHeight();

        //######################################################

        double countHeight = height * MM_TO_M;

        //полипропиленовая лента
        double polyWrapL = Math.ceil((countHeight * 1.15 * 4.0) + (2.0 * palletDepth));

        double pallet = 1.0; //шт
        double time = 14.0; //мин

        opData.setPallet(roundTo001(pallet));
        opData.setPolyWrap(roundTo001(polyWrapL));
        opData.setPackTime(roundTo001(time));
        return opData;
    }
}
