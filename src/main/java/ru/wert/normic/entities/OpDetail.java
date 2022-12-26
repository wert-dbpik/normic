package ru.wert.normic.entities;


import lombok.Getter;
import lombok.Setter;
import ru.wert.normic.entities.db_connection.Material;
import ru.wert.normic.enums.ENormType;
import ru.wert.normic.enums.EOpType;
import ru.wert.normic.interfaces.IOpWithOperations;


import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class OpDetail extends OpData implements IOpWithOperations {

    private String name = null;
    private Material material = null;
    private Integer paramA = 0;
    private Integer paramB = 0;
    private double wasteRatio = 1.1;
    private double weight = 0.0;
    private double area = 0.0;
    private List<OpData> operations = new ArrayList<>();

    public OpDetail() {
        super.normType = ENormType.NORM_DETAIL;
        super.opType = EOpType.DETAIL;
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
