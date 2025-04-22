package ru.wert.normic.controllers.electricalOperations.counters;

import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.electrical.OpConnectingDevices;
import ru.wert.normic.entities.ops.electrical.OpMountOnDin;
import ru.wert.normic.interfaces.NormCounter;

import static ru.wert.normic.AppStatics.CURRENT_BATCH;
import static ru.wert.normic.AppStatics.roundTo001;
import static ru.wert.normic.settings.NormConstants.*;

public class OpConnectingDevicesCounter implements NormCounter{

    public OpData count(OpData data){
        OpConnectingDevices opData = (OpConnectingDevices)data;

        int mortiseContact = opData.getMortiseContact(); //Врезной контакт (без снятия изоляции)
        int springClamp = opData.getSpringClamp(); //Пружинный зажим
        int clampingScrew = opData.getClampingScrew(); //Зажимной винт
        int vshg = opData.getVshg(); //ВШГ, наконечник кольцо

        boolean difficult = opData.isDifficult(); // Сложно устанавливать
        double k = difficult ? 1.3 : 1.0; //Коэффициент сложности

        //################################################################

        double timeOp =  mortiseContact * CONNECTING_DEVICES_WITH_MORTISE_CONTACT * k
                + springClamp * CONNECTING_DEVICES_WITH_SPRING_CLAMP * k
                + clampingScrew * CONNECTING_DEVICES_WITH_CLAMPING_SCREW * k
                + vshg * CONNECTING_DEVICES_WITH_VSHG * k
                ;   //мин

        double time = timeOp + timeOp * 0.084 + timeOp * 0.029 / CURRENT_BATCH;

        opData.setElectricalTime(roundTo001(time));
        return opData;
    }
}
