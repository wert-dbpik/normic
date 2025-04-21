package ru.wert.normic.controllers.electricalOperations.counters;

import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.electrical.OpMountOnDin;
import ru.wert.normic.interfaces.NormCounter;

import static ru.wert.normic.AppStatics.roundTo001;
import static ru.wert.normic.settings.NormConstants.*;

public class OpMountOnDinCounter implements NormCounter{

    public OpData count(OpData data){
        OpMountOnDin opData = (OpMountOnDin)data;

        int avtomats = opData.getAvtomats(); //Количество винтов
        int heaters = opData.getHeaters(); //Количество комплектов ВШГ
        boolean difficult = opData.isDifficult(); // Сложно устанавливать
        double k = difficult ? 1.3 : 1.0; //Коэффициент сложности

        //################################################################

        double time =  avtomats * MOUNT_ON_DIN_AUTOMATS * k
                + heaters * MOUNT_ON_DIN_HEATERS * k;   //мин

        opData.setElectricalTime(roundTo001(time));
        return opData;
    }
}
