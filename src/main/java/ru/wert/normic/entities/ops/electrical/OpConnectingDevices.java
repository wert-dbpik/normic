package ru.wert.normic.entities.ops.electrical;

import lombok.Getter;
import lombok.Setter;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.enums.ENormType;
import ru.wert.normic.enums.EOpType;

/**
 * ПОДКЛЮЧЕНИЕ ЭЛЕКТРОУСТРОЙСТВ
 */
@Getter
@Setter
public class OpConnectingDevices extends OpData {

    private int mortiseContact = 0; //Врезной контакт (без снятия изоляции)
    private int springClamp = 0; //Пружинный зажим
    private int clampingScrew = 0; //Зажимной винт
    private int vshg = 0; //ВШГ, наконечник кольцо
    private boolean difficult = false; //Трудный доступ


    public OpConnectingDevices() {
        super.normType = ENormType.NORM_ELECTRICAL;
        super.opType = EOpType.EL_CONNECTING_DEVICES;
    }

    @Override
    public String toString() {
        return String.format("Врезной контакт - %s; Пружинный зажим - %s; Зажимной винт - %s; \nВШГ(наконечник кольцо) - %s; Трудный доступ - %s",
                mortiseContact, springClamp, clampingScrew, vshg, difficult );
    }
}
