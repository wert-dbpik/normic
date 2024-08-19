package ru.wert.normic.controllers.packing.counters;

import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.opPack.OpPackInBubbleWrap;
import ru.wert.normic.enums.EWinding;
import ru.wert.normic.interfaces.NormCounter;

import static ru.wert.normic.AppStatics.roundTo001;
import static ru.wert.normic.controllers.AbstractOpPlate.MM_TO_M;
import static ru.wert.normic.settings.NormConstants.*;

public class OpPackInBubbleWrapCounter implements NormCounter {

    public OpData count(OpData data){
        OpPackInBubbleWrap opData = (OpPackInBubbleWrap)data;

        int selectedRadioButton = opData.getSelectedRadioButton();
        int height = opData.getHeight();
        int width = opData.getWidth();
        int depth = opData.getDepth();

        //######################################################

        double countHeight;
        double countDepth;
        double countWidth;

        if(selectedRadioButton == EWinding.AROUND_HEIGHT.ordinal()){
            countHeight = height * MM_TO_M;
            countDepth = depth * MM_TO_M;
            countWidth = width * MM_TO_M;
        } else if(selectedRadioButton == EWinding.AROUND_DEPTH.ordinal()){
            countHeight = depth * MM_TO_M;
            countDepth = height * MM_TO_M;
            countWidth = width * MM_TO_M;
        } else {
            countHeight = width * MM_TO_M;
            countDepth = depth * MM_TO_M;
            countWidth = height * MM_TO_M;
        }

        double bubbleWrap = Math.ceil((countWidth + countDepth) * 2.0 * countHeight  * 1.1); //м2

        double ductTape = (countHeight * 2) / DUCT_TAPE_LENGTH;  //Две высоты

        double time = BUBBLE_CUT_AND_DUCT + bubbleWrap * BUBBLE_HAND_WINDING;

        opData.setBubbleWrap(roundTo001(bubbleWrap));
        opData.setDuctTape(roundTo001(ductTape));
        opData.setPackTime(roundTo001(time));
        return opData;
    }
}
