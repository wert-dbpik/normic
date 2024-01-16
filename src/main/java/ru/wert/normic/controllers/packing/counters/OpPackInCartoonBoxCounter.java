package ru.wert.normic.controllers.packing.counters;

import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.opList.OpCutting;
import ru.wert.normic.entities.ops.opPack.OpPackInCartoonBox;
import ru.wert.normic.interfaces.NormCounter;

import static ru.wert.normic.controllers.AbstractOpPlate.MM_TO_M;
import static ru.wert.normic.settings.NormConstants.*;
import static ru.wert.normic.settings.NormConstants.PACK_IN_CARTOON_BOX_SPEED;

public class OpPackInCartoonBoxCounter  implements NormCounter {

    public OpData count(OpData data){
        OpPackInCartoonBox opData = (OpPackInCartoonBox)data;

        int height = opData.getHeight();
        int width = opData.getWidth();
        int depth = opData.getDepth();
        int partMin = opData.getPartMin();

        //######################################################

        double countHeight = height * MM_TO_M;
        double countDepth = depth * MM_TO_M;
        double countWidth = width * MM_TO_M;

        double cartoon = Math.ceil((countWidth + 0.1) * (countDepth + 0.1) * 2 +
                (countWidth + 0.1) * (countHeight + 0.1) * 2 +
                (countDepth + 0.1) * (countHeight + 0.1) * 2);

        double ductTape = ((countWidth + countDepth) * 4.0 + countHeight * 4.0) / DUCT_TAPE_LENGTH;

        double time = (CARTOON_BOX_SPEED + CARTOON_BOX_PREPARED_TIME / partMin) * 1.07 + //Время изготовления коробки
                PACK_IN_CARTOON_BOX_SPEED; //Время упаковки изделия в коробку

        opData.setCartoon(cartoon);
        opData.setDuctTape(ductTape);
        opData.setPackTime(time);
        return opData;
    }
}
