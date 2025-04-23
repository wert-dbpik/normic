package ru.wert.normic.controllers.electricalOperations.counters;

import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.electrical.OpConnectDeviceClampingScrew;
import ru.wert.normic.entities.ops.electrical.OpConnectDeviceMortiseContact;
import ru.wert.normic.interfaces.NormCounter;

import static ru.wert.normic.AppStatics.CURRENT_BATCH;
import static ru.wert.normic.AppStatics.roundTo001;
import static ru.wert.normic.settings.NormConstants.*;

public class OpConnectDeviceClampingScrewCounter implements NormCounter{

    public OpData count(OpData data){
        OpConnectDeviceClampingScrew opData = (OpConnectDeviceClampingScrew)data;

        int clampingScrew = opData.getClampingScrew(); //Зажимной винт

        boolean difficult = opData.isDifficult(); // Сложно устанавливать
        double k = difficult ? 1.3 : 1.0; //Коэффициент сложности

        //################################################################

        double timeOp = clampingScrew * CONNECTING_DEVICES_WITH_CLAMPING_SCREW * k
                ;   //мин

        double time = timeOp + timeOp * 0.084 + timeOp * 0.029 / CURRENT_BATCH;

        opData.setElectricalTime(roundTo001(time));
        return opData;
    }
}
