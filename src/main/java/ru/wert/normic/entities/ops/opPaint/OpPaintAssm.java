package ru.wert.normic.entities.ops.opPaint;

import lombok.Getter;
import lombok.Setter;
import ru.wert.normic.controllers.paint.counters.OpPaintAssmCounter;
import ru.wert.normic.controllers.paint.counters.OpPaintCounter;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.single.OpAssm;
import ru.wert.normic.enums.EAssemblingType;
import ru.wert.normic.enums.EColor;
import ru.wert.normic.enums.ENormType;
import ru.wert.normic.enums.EOpType;
import ru.wert.normic.interfaces.IOpWithOperations;
import ru.wert.normic.interfaces.NormCounter;

import static ru.wert.normic.controllers.AbstractOpPlate.DECIMAL_FORMAT;

/**
 * ОКРАШИВАНИЕ СБОРОЧНОЙ ЕДИНИЦЫ
 */
@Getter
@Setter
public class OpPaintAssm extends OpData {

    private transient NormCounter normCounter = new OpPaintAssmCounter();

    private OpAssm assm; //ссылка на окрашиваемую сборку
    private EColor color = EColor.COLOR_I;
    private boolean twoSides = true;
    private double dyeWeight = 0.0;
    private boolean calculatedArea = true; //использовать расчитанную площадь спокрытия
    private double countedArea = 0.0; //расчетная площадь сборки
    private double area = 0.0; //Площадь покрытия введенная пользователем
    private Integer along = 0; //длина сборки вдоль штанги
    private Integer across = 0; //длина сборки поперек штанги
    private EAssemblingType assmType = EAssemblingType.SOLID; //Тип сборочной единицы

    public OpPaintAssm() {
        super.normType = ENormType.NORM_PAINTING;
        super.opType = EOpType.PAINT_ASSM;
    }

    @Override
    public String toString() {
        return "цвет = " + color.getRal() +
                ", с двух сторон = " + twoSides +
                ", М краски = " + DECIMAL_FORMAT.format(dyeWeight) + " кг." +
                ",\nS покр. = " + DECIMAL_FORMAT.format(area) + " мм.кв." +
                ", А(вдоль) = " + along + " мм." +
                ", В(поперек) = " + across + " мм." +
                ", \nтип сборочной единицы = " + assmType.getName();
    }
}
