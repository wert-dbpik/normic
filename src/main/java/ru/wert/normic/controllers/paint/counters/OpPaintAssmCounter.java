package ru.wert.normic.controllers.paint.counters;

import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.opPaint.OpPaintAssm;
import ru.wert.normic.entities.ops.single.OpAssm;
import ru.wert.normic.entities.ops.single.OpDetail;
import ru.wert.normic.enums.EColor;
import ru.wert.normic.interfaces.IOpWithOperations;
import ru.wert.normic.interfaces.NormCounter;

import java.util.List;

import static ru.wert.normic.controllers.AbstractOpPlate.DOUBLE_FORMAT;
import static ru.wert.normic.settings.NormConstants.*;

public class OpPaintAssmCounter implements NormCounter {

    private OpPaintAssm opData;
    private OpAssm assm;
    private double finalPaintedArea;
    private boolean useCalculatedArea;
    private double kArea;

    public OpData count(OpData data) {
        opData = (OpPaintAssm) data;

        assm = opData.getAssm();

        EColor color = opData.getColor(); //Цвет краски
        int along = opData.getAlong(); //Параметр А вдоль штанги
        int across = opData.getAcross(); //Параметр B поперек штанги
        double area = opData.getArea(); //Площадь покрытия введенная вручную
        double pantingSpeed = opData.getAssmType().getSpeed();// Скорость нанесения покрытия
        boolean twoSides = opData.isTwoSides(); //Красить с двух сторон

        this.useCalculatedArea = opData.isCalculatedArea(); //Использовать расчетную площадь окрашивания
        this.kArea = twoSides ? 1.0 : 0.5;
        //###########################################################################

        double countedArea = countCalculatedArea(assm);
        finalPaintedArea = useCalculatedArea ? countedArea * kArea : area;


        double dyeWeight = color.getConsumption() * 0.001 * finalPaintedArea;

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
                + finalPaintedArea * WINDING_MOVING_SPEED //Время подготовки к окрашиванию
                + finalPaintedArea * pantingSpeed //Время нанесения покрытия
                + 40.0/bakeBars/partsOnBar;  //Время полимеризации
        if(finalPaintedArea == 0.0) time = 0.0;

        opData.setCountedArea(countedArea);
        opData.setDyeWeight(dyeWeight);
        opData.setPaintTime(time);
        return opData;
    }

    private double countCalculatedArea(IOpWithOperations assm){
        double area = 0.0;
        List<OpData> ops = assm.getOperations();
        for (OpData op : ops) {
            if (op instanceof IOpWithOperations) countCalculatedArea((IOpWithOperations) op);
            if (op instanceof OpDetail){
                area += ((OpDetail) op).getArea()* op.getQuantity();//количество деталей в сборке
            }
        }
//        formAreaProperty.set(area); //здесь надо разделить на количество сборок в изделии, если делался импорт из Excel
        return area;
    }


}
