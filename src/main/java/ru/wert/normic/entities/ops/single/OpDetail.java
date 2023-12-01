package ru.wert.normic.entities.ops.single;


import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import lombok.Getter;
import lombok.Setter;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.db_connection.material.Material;
import ru.wert.normic.enums.ENormType;
import ru.wert.normic.enums.EOpType;
import ru.wert.normic.interfaces.IOpWithOperations;


import java.util.ArrayList;
import java.util.List;

import static ru.wert.normic.controllers.AbstractOpPlate.DECIMAL_FORMAT;

/**
 * ДОБАВЛЕНИЕ ДЕТАЛИ
 */
@Getter
@Setter
public class OpDetail extends OpData implements IOpWithOperations {

    private BooleanProperty doneProperty = new SimpleBooleanProperty(false); //Расчет детали завершен
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

    @Override
    public String toString() {
        return "Материал = " + (material == null ? "нет" : material.getName()) +
                ", A = " + paramA + " мм" +
                ", B = " + paramB + " мм" +
                ", M = " + DECIMAL_FORMAT.format(weight) + " кг." +
                ", S покр =" + DECIMAL_FORMAT.format(area) + " м.кв.";
    }

    /**
     * Метод возвращает данные по операции, в том числе и нормы времени по участкам
     * Используется для восстановления закрытых плашек и сохранения расчетов
     */
    @Override
    public OpData getOpData() {
        return this;
    }


    @Override
    public void setDone(boolean val) {
        doneProperty.set(val);
    }
}
