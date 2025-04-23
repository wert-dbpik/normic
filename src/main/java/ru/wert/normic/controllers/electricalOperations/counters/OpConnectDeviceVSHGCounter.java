package ru.wert.normic.controllers.electricalOperations.counters;

import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.electrical.OpConnectDeviceMortiseContact;
import ru.wert.normic.entities.ops.electrical.OpConnectDeviceVSHG;
import ru.wert.normic.interfaces.NormCounter;

import static ru.wert.normic.AppStatics.CURRENT_BATCH;
import static ru.wert.normic.AppStatics.roundTo001;
import static ru.wert.normic.settings.NormConstants.*;

public class OpConnectDeviceVSHGCounter implements NormCounter{

    public OpData count(OpData data){
        OpConnectDeviceVSHG opData = (OpConnectDeviceVSHG)data;

        int vshg = opData.getVshg(); //ВШГ, наконечник кольцо

        boolean difficult = opData.isDifficult(); // Сложно устанавливать
        double k = difficult ? 1.3 : 1.0; //Коэффициент сложности

        //################################################################

        double timeOp =  vshg * CONNECTING_DEVICES_WITH_VSHG * k
                ;   //мин

        double time = timeOp + timeOp * 0.084 + timeOp * 0.029 / CURRENT_BATCH;

        opData.setElectricalTime(roundTo001(time));
        return opData;
    }
}
