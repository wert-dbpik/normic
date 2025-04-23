package ru.wert.normic.entities.ops.electrical;

import lombok.Getter;
import lombok.Setter;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.enums.ENormType;
import ru.wert.normic.enums.EOpType;

/**
 * ПОДКЛЮЧЕНИЕ ЭЛЕКТРОУСТРОЙСТВ НА ПРУЖИННЫЙ ЗАЖИМ
 */
@Getter
@Setter
public class OpConnectDeviceSpringClamp extends OpData {

    private String name = ""; //Наименование
    private int springClamp = 0; //Пружинный зажим
    private boolean difficult = false; //Трудный доступ


    public OpConnectDeviceSpringClamp() {
        super.normType = ENormType.NORM_ELECTRICAL;
        super.opType = EOpType.EL_CONNECT_DEVICE_SPRING_CLAMP;
    }

    @Override
    public String toString() {
        return String.format("Наименование - %s; Пружинный зажим - %s; Трудный доступ - %s",
                name, springClamp, difficult );
    }
}
