package ru.wert.normic.dataBaseEntities.ops.opPaint;

import lombok.Getter;
import lombok.Setter;
import ru.wert.normic.dataBaseEntities.db_connection.material.Material;
import ru.wert.normic.dataBaseEntities.ops.OpData;
import ru.wert.normic.enums.*;

import static ru.wert.normic.controllers.AbstractOpPlate.DECIMAL_FORMAT;

/**
 * ОКРАШИВАНИЕ ЛИСТОВОЙ ДЕТАЛИ
 */
@Getter
@Setter
public class OpPaint extends OpData {

    private Material material; //материал
    private int razvA = 0; //размер развертки А
    private int razvB = 0; //размер развертки B
    private boolean twoSides = true; //Окрашивание с двух сторон

    private EColor color = EColor.COLOR_I;
    private double area = 0.0;
    private double dyeWeight = 0.0;
    private Integer along = 0;
    private Integer across = 0;
    private EPaintingDifficulty difficulty = EPaintingDifficulty.SIMPLE;
    private Integer hangingTime = 20;

    public OpPaint() {
        super.normType = ENormType.NORM_PAINTING;
        super.opType = EOpType.PAINTING;
    }

    @Override
    public String toString() {
        return "цвет = " + color.getRal() +
                ", S покр = " + DECIMAL_FORMAT.format(area) + " мм.кв." +
                ", с двух сторон = " + twoSides +
                ", М краски = " + DECIMAL_FORMAT.format(dyeWeight) + " кг." +
                ",\nА(вдоль) = " + along + " мм." +
                ", В(поперек) = " + across + " мм." +
                ", сложность = " + difficulty.getDifficultyName() +
                ", время навешивания = " + hangingTime + " сек.";
    }
}
