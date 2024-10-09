package ru.wert.normic.entities.ops.opPaint;

import lombok.Getter;
import lombok.Setter;
import ru.wert.normic.entities.db_connection.material.Material;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.single.OpAssm;
import ru.wert.normic.enums.EAssemblingType;
import ru.wert.normic.enums.EColor;
import ru.wert.normic.enums.ENormType;
import ru.wert.normic.enums.EOpType;

import static ru.wert.normic.controllers.AbstractOpPlate.DECIMAL_FORMAT;

/**
 * ОКРАШИВАНИЕ СБОРОЧНОЙ ЕДИНИЦЫ
 */
@Getter
@Setter
public class OpPaintDetail extends OpData {

    private Material material; //материал
    private int razvA = 0; //размер развертки А
    private int razvB = 0; //размер развертки B
    private EColor color = EColor.COLOR_I;
    private boolean twoSides = true;
    private double dyeWeight = 0.0;
    private double countedArea = 0.0; //расчетная площадь сборки
    private Integer along = 0; //длина сборки вдоль штанги
    private Integer across = 0; //длина сборки поперек штанги
    private EAssemblingType assmType = EAssemblingType.SOLID; //Тип сборочной единицы

    public OpPaintDetail() {
        super.normType = ENormType.NORM_PAINTING;
        super.opType = EOpType.PAINT_DETAIL;
    }

    @Override
    public String toString() {
        return "цвет = " + color.getRal() +
                ", с двух сторон = " + twoSides +
                ", М краски = " + DECIMAL_FORMAT.format(dyeWeight) + " кг." +
                ",\nS покр. = " + DECIMAL_FORMAT.format(countedArea) + " мм.кв." +
                ", А(вдоль) = " + along + " мм." +
                ", В(поперек) = " + across + " мм." +
                ", \nтип сборочной единицы = " + assmType.getName();
    }
}
