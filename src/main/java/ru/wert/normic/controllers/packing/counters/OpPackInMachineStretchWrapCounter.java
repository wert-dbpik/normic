package ru.wert.normic.controllers.packing.counters;

import ru.wert.normic.dataBaseEntities.ops.OpData;
import ru.wert.normic.dataBaseEntities.ops.opPack.OpPackInMachineStretchWrap;
import ru.wert.normic.interfaces.NormCounter;

import static ru.wert.normic.AppStatics.roundTo001;
import static ru.wert.normic.controllers.AbstractOpPlate.MM_TO_M;
import static ru.wert.normic.settings.NormConstants.*;
import static ru.wert.normic.settings.NormConstants.STRETCH_MACHINE_WINDING;

public class OpPackInMachineStretchWrapCounter implements NormCounter {

    public OpData count(OpData data){
        OpPackInMachineStretchWrap opData = (OpPackInMachineStretchWrap)data;

        int partMin = opData.getPartMin();
        int height = opData.getHeight();
        int width = opData.getWidth();
        int depth = opData.getDepth();

        //######################################################

        double countHeight = height * MM_TO_M;
        double countDepth = depth * MM_TO_M;
        double countWidth = width * MM_TO_M;

        double cartoonTop = Math.ceil((countWidth + 0.1) * (countDepth + 0.1) * 1.2 * 2); //Крышки верх и низ
        double cartoonAngle = Math.ceil(countHeight * 1.1 * 4); //4 уголка на всю высоту

        double stretchWrap = Math.ceil((countWidth + countDepth) * 2 * countHeight / 0.3 * 2); //м

        double perimeter = (countWidth + countDepth) * 2;
        double ductTape = Math.ceil(perimeter * 4) / DUCT_TAPE_LENGTH;  //Вокруг изделия 4 раза

        double time = CARTOON_BOX_AND_ANGLES_SPEED + CARTOON_BOX_PREPARED_TIME / partMin * 1.07 + //Время изготовления 2х крышек
                STRETCH_MACHINE_WINDING * countHeight; //Время упаковки изделия в коробку

        opData.setCartoon(roundTo001(cartoonTop));
        opData.setCartoonAngle(roundTo001(cartoonAngle));
        opData.setStretchMachineWrap(roundTo001(stretchWrap));
        opData.setDuctTape(roundTo001(ductTape));
        opData.setPackTime(roundTo001(time));
        return opData;
    }
}
