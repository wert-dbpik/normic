package ru.wert.normic.entities.ops.electrical;

import lombok.Getter;
import lombok.Setter;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.enums.ENormType;
import ru.wert.normic.enums.EOpType;

/**
 * ПОДКЛЮЧЕНИЕ ЭЛЕКТРОУСТРОЙСТВ НА ЗАЖИМНОЙ ВИНТ
 */
@Getter
@Setter
public class OpConnectDeviceClampingScrew extends OpData {

    private String name = ""; //Наименование
    private int clampingScrew = 0; //Зажимной винт
    private boolean difficult = false; //Трудный доступ


    public OpConnectDeviceClampingScrew() {
        super.normType = ENormType.NORM_ELECTRICAL;
        super.opType = EOpType.EL_CONNECT_DEVICE_CLAMPING_SCREW;
    }

    @Override
    public String toString() {
        return String.format("Наименование - %s; Зажимной винт - %s; Трудный доступ - %s",
                name, clampingScrew, difficult );
    }
}
