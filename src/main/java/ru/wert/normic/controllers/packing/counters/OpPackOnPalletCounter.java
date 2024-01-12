package ru.wert.normic.controllers.packing.counters;

import ru.wert.normic.entities.ops.opPack.OpPackInBubbleWrap;
import ru.wert.normic.entities.ops.opPack.OpPackOnPallet;

import static ru.wert.normic.controllers.AbstractOpPlate.MM_TO_M;

public class OpPackOnPalletCounter {

    private static double palletDepth = 0.800; //габарит квадратного поддона
    private static double palletWidth = 1.200; //габарит квадратного поддона

    public static OpPackOnPallet count(OpPackOnPallet opData){

        int height = opData.getHeight();

        //######################################################

        double countHeight = height * MM_TO_M;

        //полипропиленовая лента
        double polyWrapL = Math.ceil((countHeight * 1.15 * 4.0) + (2.0 * palletDepth));

        double pallet = 1.0;
        double time = 14.0;

        opData.setPallet(pallet);
        opData.setPolyWrap(polyWrapL);
        opData.setPackTime(time);
        return opData;
    }
}
