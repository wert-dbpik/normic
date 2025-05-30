package ru.wert.normic.controllers.electricalOperations.counters;

import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.electrical.OpMountOnDinAutomats;
import ru.wert.normic.entities.ops.electrical.OpMountOnDinHeaters;
import ru.wert.normic.interfaces.NormCounter;

import static ru.wert.normic.AppStatics.CURRENT_BATCH;
import static ru.wert.normic.AppStatics.roundTo001;
import static ru.wert.normic.settings.NormConstants.MOUNT_ON_DIN_AUTOMATS;
import static ru.wert.normic.settings.NormConstants.MOUNT_ON_DIN_HEATERS;

public class OpMountOnDinHeatersCounter implements NormCounter{

    public OpData count(OpData data){
        OpMountOnDinHeaters opData = (OpMountOnDinHeaters)data;

        int heaters = opData.getHeaters(); //Количество комплектов ВШГ
        boolean difficult = opData.isDifficult(); // Сложно устанавливать
        double k = difficult ? 1.3 : 1.0; //Коэффициент сложности

        //################################################################

        double timeOp =  heaters * MOUNT_ON_DIN_HEATERS * k;   //мин

        double time = timeOp + timeOp * 0.084 + timeOp * 0.029 / CURRENT_BATCH;

        opData.setElectricalTime(roundTo001(time));
        return opData;
    }
}
