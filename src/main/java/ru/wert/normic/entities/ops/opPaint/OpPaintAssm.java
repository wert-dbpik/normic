package ru.wert.normic.entities.ops.opPaint;

import lombok.Getter;
import lombok.Setter;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.enums.EAssemblingType;
import ru.wert.normic.enums.EColor;
import ru.wert.normic.enums.ENormType;
import ru.wert.normic.enums.EOpType;

/**
 * ОКРАШИВАНИЕ СБОРОЧНОЙ ЕДИНИЦЫ
 */
@Getter
@Setter
public class OpPaintAssm extends OpData {

    private EColor color = EColor.COLOR_I;
    private boolean twoSides = true;
    private double dyeWeight = 0.0;
    private boolean calculatedArea = true; //использовать расчитанную площадь спокрытия
    private double area = 0.0; //Площадь покрытия введенная пользователем
    private Integer along = 0; //длина сборки вдоль штанги
    private Integer across = 0; //длина сборки поперек штанги
    private EAssemblingType assmType = EAssemblingType.SOLID; //Тип сборочной единицы

    public OpPaintAssm() {
        super.normType = ENormType.NORM_PAINTING;
        super.opType = EOpType.PAINT_ASSM;
    }
}
