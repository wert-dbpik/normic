package ru.wert.normic.entities.ops.single;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import lombok.Getter;
import lombok.Setter;
import ru.wert.normic.controllers.AbstractOpPlate;
import ru.wert.normic.controllers._forms.AbstractFormController;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.enums.ENormType;
import ru.wert.normic.enums.EOpType;
import ru.wert.normic.interfaces.IOpWithOperations;
import ru.wert.normic.interfaces.Paintable;

import java.util.ArrayList;
import java.util.List;

/**
 * ДОБАВЛЕНИЕ СБОРКИ
 */
@Getter
@Setter
public class OpAssm extends OpData implements IOpWithOperations, Paintable {

    private transient AbstractOpPlate opPlate;
    private transient AbstractFormController formController;
    private transient OpAssm painter;

    private boolean done = false;
    private transient BooleanProperty doneProperty = new SimpleBooleanProperty(done); //Расчет сборки завершен
    private String name = null;
    private double area = 0.0;
    private List<OpData> operations = new ArrayList<>();

    public OpAssm() {
        super.normType = ENormType.NORM_ASSEMBLE;
        super.opType = EOpType.ASSM;

        doneProperty.addListener((observable, oldValue, newValue) -> done = newValue);
    }

    @Override
    public String toString() {
        return "";
//                "Площадь покрытия S = " + DECIMAL_FORMAT.format(area) + " м.кв.";

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
