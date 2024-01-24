package ru.wert.normic.controllers.paint.counters;

import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.opPaint.OpPaintAssm;
import ru.wert.normic.enums.EColor;
import ru.wert.normic.interfaces.NormCounter;

import static ru.wert.normic.settings.NormConstants.*;

public class OpPaintAssmCounter implements NormCounter {

    public OpData count(OpData data) {
        OpPaintAssm opData = (OpPaintAssm) data;

        EColor color = opData.getColor(); //Цвет краски
        int along = opData.getAlong(); //Параметр А вдоль штанги
        int across = opData.getAcross(); //Параметр B поперек штанги
        double area = opData.getArea(); //Площадь покрытия введенная вручную
        double pantingSpeed = opData.getAssmType().getSpeed();// Скорость нанесения покрытия
        boolean twoSides = opData.isTwoSides(); //Красить с двух сторон
        boolean useCalculatedArea = opData.isCalculatedArea(); //Использовать расчетную площадь окрашивания

        //###########################################################################

        double dyeWeight = color.getConsumption() * 0.001 * area;

        final int alongSize = along + ASSM_DELTA;
        final int acrossSize = across + ASSM_DELTA;

        int partsOnBar = 2500/alongSize;

        //Количество штанг в печи
        int bakeBars;
        if(acrossSize < 49) bakeBars = 6;
        else if(acrossSize >= 50 && acrossSize <= 99) bakeBars = 5;
        else if(acrossSize >= 100 && acrossSize <= 199) bakeBars = 4;
        else if(acrossSize >= 200 && acrossSize <= 299) bakeBars = 3;
        else if(acrossSize >= 300 && acrossSize <= 399) bakeBars = 2;
        else bakeBars = 1;

        double time;
        time = HANGING_TIME//Время навешивания
                + area * WINDING_MOVING_SPEED //Время подготовки к окрашиванию
                + area * pantingSpeed //Время нанесения покрытия
                + 40.0/bakeBars/partsOnBar;  //Время полимеризации
        if(area == 0.0) time = 0.0;

        opData.setDyeWeight(dyeWeight);
        opData.setPaintTime(time);
        return opData;
    }
}
