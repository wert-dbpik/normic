package ru.wert.normic.controllers.electricalOperations.counters;

import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.electrical.OpConnectingDevices;
import ru.wert.normic.entities.ops.electrical.OpCutCableHandly;
import ru.wert.normic.interfaces.NormCounter;

import static ru.wert.normic.AppStatics.CURRENT_BATCH;
import static ru.wert.normic.AppStatics.roundTo001;
import static ru.wert.normic.settings.NormConstants.*;

public class OpCutCableHandlyCounter implements NormCounter{

    public OpData count(OpData data){
        OpCutCableHandly opData = (OpCutCableHandly)data;

        int multiCoreCable6mm = opData.getMultiCoreCable6mm(); //Многожильный провод Дн=6 мм
        int multiCoreCable11mm = opData.getMultiCoreCable11mm(); //Многожильный провод Дн=11..15 мм
        int singleCoreCable = opData.getSingleCoreCable(); //Одножильный провод

        boolean difficult = opData.isDifficult(); // Сложно устанавливать
        double k = difficult ? 1.2 : 1.0; // Резка проводов по месту, К = 1,2

        //################################################################

        double timeOp =  multiCoreCable6mm * CUTTING_MULTI_CORE_CABLE_6MM* k
                + multiCoreCable11mm * CUTTING_MULTI_CORE_CABLE_11MM * k
                + singleCoreCable * CUTTING_SINGLE_CORE_CABLE * k
                ;   //мин

        double time = timeOp + timeOp * 0.084 + timeOp * 0.029 / CURRENT_BATCH;

        opData.setElectricalTime(roundTo001(time));
        return opData;
    }
}
