package ru.wert.normic.entities.ops.electrical;

import lombok.Getter;
import lombok.Setter;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.enums.ENormType;
import ru.wert.normic.enums.EOpType;

/**
 * ПОДКЛЮЧЕНИЕ ЭЛЕКТРОУСТРОЙСТВ ВРЕЗНОЙ КОНТАКТ
 */
@Getter
@Setter
public class OpConnectDeviceMortiseContact extends OpData {

    private String name = ""; //Примечание, наименование устройства
    private int mortiseContact = 0; //Врезной контакт (без снятия изоляции)
    private boolean difficult = false; //Трудный доступ


    public OpConnectDeviceMortiseContact() {
        super.normType = ENormType.NORM_ELECTRICAL;
        super.opType = EOpType.EL_CONNECT_DEVICE_MORTISE_CONTACT;
    }

    @Override
    public String toString() {
        return String.format("Примечание - %s; \nврезной контакт - %s; трудный доступ - %s",
                name, mortiseContact, difficult );
    }
}
