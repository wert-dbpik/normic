package ru.wert.normic.controllers.electricalOperations.counters;

import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.electrical.OpCutCableHandlyMC6;
import ru.wert.normic.interfaces.NormCounter;

import static ru.wert.normic.AppStatics.CURRENT_BATCH;
import static ru.wert.normic.AppStatics.roundTo001;
import static ru.wert.normic.settings.NormConstants.*;

public class OpCutCableHandlyMC6Counter implements NormCounter{

    public OpData count(OpData data){
        OpCutCableHandlyMC6 opData = (OpCutCableHandlyMC6)data;

        int multiCoreCable6mm = opData.getMultiCoreCable6mm(); //Многожильный провод Дн=6 мм

        boolean difficult = opData.isDifficult(); // Сложно устанавливать
        double k = difficult ? 1.2 : 1.0; // Резка проводов по месту, К = 1,2

        //################################################################

        double timeOp =  multiCoreCable6mm * CUT_CABLE_HANDLY_MC6 * k
                ;   //мин

        double time = timeOp + timeOp * 0.084 + timeOp * 0.029 / CURRENT_BATCH;

        opData.setElectricalTime(roundTo001(time));
        return opData;
    }
}
