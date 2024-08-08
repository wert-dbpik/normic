package ru.wert.normic.dataBaseEntities.ops.single;


import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import lombok.Getter;
import lombok.Setter;
import ru.wert.normic.controllers.AbstractOpPlate;
import ru.wert.normic.dataBaseEntities.ops.OpData;
import ru.wert.normic.dataBaseEntities.db_connection.material.Material;
import ru.wert.normic.enums.ENormType;
import ru.wert.normic.enums.EOpType;
import ru.wert.normic.enums.EPieceMeasurement;
import ru.wert.normic.interfaces.IOpWithOperations;
import ru.wert.normic.interfaces.Paintable;


import java.util.ArrayList;
import java.util.List;

import static ru.wert.normic.controllers.AbstractOpPlate.DECIMAL_FORMAT;

/**
 * ДОБАВЛЕНИЕ ДЕТАЛИ
 */
@Getter
@Setter
public class OpDetail extends OpData implements IOpWithOperations, Paintable {

    private transient AbstractOpPlate opPlate;
    private transient OpAssm painter;

    private boolean done = false;
    private transient BooleanProperty doneProperty = new SimpleBooleanProperty(done);
    private String name = null;
    private Material material = null;
    private Integer paramA = 0;
    private Integer paramB = 0;
    private Integer paramC = 0; //Дополнительный параметр для материала штучного типа
    private double wasteRatio = 1.1;
    private double weight = 0.0; //Для штучных материалов промежуточное значение - фактический расход в ед.изм.
    private double pieceOutlay = 0.0; //Для штучных расход материала в штуках
    private double area = 0.0;
    private transient EPieceMeasurement measurement = null; //Поле для штучных материалов
    private List<OpData> operations = new ArrayList<>();

    public OpDetail() {
        super.normType = ENormType.NORM_DETAIL;
        super.opType = EOpType.DETAIL;

        doneProperty.addListener((observable, oldValue, newValue) -> done = newValue);
    }

    @Override
    public String toString() {
        return "Материал = " + (material == null ? "нет" : material.getName()) +
                ", A = " + paramA + " мм" +
                ", B = " + paramB + " мм" +
                ", C = " + paramC + " мм" +
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
        done = val;
        doneProperty.set(val);
    }

    @Override
    public boolean isDone() {
        return done;
    }

}
