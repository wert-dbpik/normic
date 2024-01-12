package ru.wert.normic.controllers.packing.counters;

import ru.wert.normic.entities.ops.opPack.OpPackInHandStretchWrap;
import ru.wert.normic.enums.EWinding;

import static ru.wert.normic.controllers.AbstractOpPlate.MM_TO_M;
import static ru.wert.normic.settings.NormConstants.DUCT_TAPE_LENGTH;
import static ru.wert.normic.settings.NormConstants.STRETCH_HAND_WINDING;

public class OpPackInHandStretchWrapCounter {

    public static OpPackInHandStretchWrap count(OpPackInHandStretchWrap opData){
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

        double handStretchWrap = Math.ceil((countWidth + countDepth) * 2.0 * countHeight/0.3  * 2); //м

        double ductTape = countHeight / DUCT_TAPE_LENGTH;  //1 высота

        double time = handStretchWrap * STRETCH_HAND_WINDING;

        opData.setStretchHandWrap(handStretchWrap);
        opData.setDuctTape(ductTape);
        opData.setPackTime(time);
        return opData;
    }
}
