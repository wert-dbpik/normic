package ru.wert.normic.entities.ops.electrical;

import lombok.Getter;
import lombok.Setter;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.enums.ENormType;
import ru.wert.normic.enums.EOpType;

/**
 * ПОДКЛЮЧЕНИЕ ЭЛЕКТРОУСТРОЙСТВ НА ВШГ
 */
@Getter
@Setter
public class OpConnectDeviceVSHG extends OpData {

    private String name = ""; //Наименование
    private int vshg = 0; //ВШГ, наконечник кольцо
    private boolean difficult = false; //Трудный доступ


    public OpConnectDeviceVSHG() {
        super.normType = ENormType.NORM_ELECTRICAL;
        super.opType = EOpType.EL_CONNECT_DEVICE_VSHG;
    }

    @Override
    public String toString() {
        return String.format("Наименование - %s; ВШГ(наконечник кольцо) - %s; Трудный доступ - %s",
                name, vshg, difficult );
    }
}
