package ru.wert.normic.entities.ops.single;


import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import lombok.Getter;
import lombok.Setter;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.enums.ENormType;
import ru.wert.normic.enums.EOpType;
import ru.wert.normic.interfaces.IOpWithOperations;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class OpPack extends OpData implements IOpWithOperations {

    private boolean done = false;
    private transient BooleanProperty doneProperty = new SimpleBooleanProperty(done);
    private String name = null;
    private Integer width = 0;
    private Integer depth = 0;
    private Integer height = 0;

    private double weight = 0.0;
    private double area = 0.0;
    private List<OpData> operations = new ArrayList<>();

    public OpPack() {
        super.normType = ENormType.NORM_PACKING;
        super.opType = EOpType.PACK;

        doneProperty.addListener((observable, oldValue, newValue) -> done = newValue);
    }

    @Override
    public String toString() {
        return "Габариты : Ш = " + width + " мм" +
                ", Г = " + depth + " мм" +
                ", В = " + height + " мм" ;
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
        doneProperty.setValue(val);
    }

    @Override
    public boolean isDone() {

        return done;
    }
}
