package ru.wert.normic.entities;

import lombok.Getter;
import lombok.Setter;
import ru.wert.normic.enums.ENormType;
import ru.wert.normic.enums.EOpType;
import ru.wert.normic.interfaces.IOpWithOperations;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class OpAssm extends OpData  implements IOpWithOperations {

    private String name = null;
    private double area = 0.0;
    private List<OpData> operations = new ArrayList<>();

    public OpAssm() {
        super.normType = ENormType.NORM_ASSEMBLE;
        super.opType = EOpType.ASSM;
    }

    /**
     * Метод возвращает данные по операции, в том числе и нормы времени по участкам
     * Используется для восстановления закрытых плашек и сохранения расчетов
     */
    @Override
    public OpData getOpData() {
        return this;
    }
}
