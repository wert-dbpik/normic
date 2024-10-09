package ru.wert.normic.controllers.paint.counters;

import javafx.application.Platform;
import ru.wert.normic.decoration.warnings.Warning2;
import ru.wert.normic.entities.db_connection.material.Material;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.opPaint.OpPaintNew;
import ru.wert.normic.entities.ops.single.OpAssm;
import ru.wert.normic.entities.ops.single.OpDetail;
import ru.wert.normic.enums.EColor;
import ru.wert.normic.enums.EMatType;
import ru.wert.normic.enums.EOpType;
import ru.wert.normic.interfaces.IOpWithOperations;
import ru.wert.normic.interfaces.NormCounter;
import ru.wert.normic.interfaces.Paintable;

import java.util.ArrayList;
import java.util.List;

import static ru.wert.normic.AppStatics.MAIN_CONTROLLER;
import static ru.wert.normic.AppStatics.roundTo001;
import static ru.wert.normic.controllers.AbstractOpPlate.MM2_TO_M2;
import static ru.wert.normic.settings.NormConstants.*;

public class OpPaintNewCounter implements NormCounter {

    private double countedArea; //Переменная для расчета площади покрытия
    //Список операций с дублирующей покраской, создан чтобы сообщения не доставали пользователя
    List<IOpWithOperations> opsWithDoublePainting = new ArrayList<>();

    public OpData count(OpData data) {
        OpPaintNew opData = (OpPaintNew) data;

        int razvA = opData.getRazvA();
        int razvB = opData.getRazvB();
        Material material = opData.getMaterial();
        EColor color = opData.getColor(); //Цвет краски
        int along = opData.getAlong(); //Параметр А вдоль штанги
        int across = opData.getAcross(); //Параметр B поперек штанги
        double pantingSpeed = opData.getAssmType().getSpeed();// Скорость нанесения покрытия
        boolean twoSides = opData.isTwoSides(); //Красить с двух сторон

        //###########################################################################

        //Определяем площадь покрытия
        if (material.getMatType().getName().equals(EMatType.LIST.getMatTypeName())) {
            double kArea = twoSides ? 1.0 : 0.5;
            countedArea = razvA * razvB * 2 * MM2_TO_M2 * kArea; //Площадь покрытия
        } else {
            //Масса погонного метра
            double diameter = material.getParamS();
            countedArea = 3.14 * diameter * (razvA + razvB) * MM2_TO_M2;
        }

        double dyeWeight = (color.getConsumption() * 0.001) * countedArea;

        final int alongSize = along + ASSM_DELTA;
        final int acrossSize = across + ASSM_DELTA;

        int partsOnBar = 2500 / alongSize;

        //Количество штанг в печи
        int bakeBars;
        if (acrossSize < 49) bakeBars = 6;
        else if (acrossSize >= 50 && acrossSize <= 99) bakeBars = 5;
        else if (acrossSize >= 100 && acrossSize <= 199) bakeBars = 4;
        else if (acrossSize >= 200 && acrossSize <= 299) bakeBars = 3;
        else if (acrossSize >= 300 && acrossSize <= 399) bakeBars = 2;
        else bakeBars = 1;

        //Внутренняя ширина печи 1560 мм
/*        int bakeBars;
        if (acrossSize < 259) bakeBars = 6;
        else if (acrossSize >= 260 && acrossSize <= 311) bakeBars = 5;
        else if (acrossSize >= 312 && acrossSize <= 389) bakeBars = 4;
        else if (acrossSize >= 390 && acrossSize <= 519) bakeBars = 3;
        else if (acrossSize >= 520 && acrossSize <= 779) bakeBars = 2;
        else bakeBars = 1;*/

        //Для цветной краски максимальное количество штанг 4
        if(bakeBars > 4 && !color.getRal().contains("7035")) bakeBars = 4;

        double time;
        time = HANGING_TIME//Время навешивания
                + countedArea * WINDING_MOVING_SPEED //Время подготовки к окрашиванию
                + countedArea * pantingSpeed //Время нанесения покрытия
                + 40.0 / bakeBars / partsOnBar;  //Время полимеризации
        if (countedArea == 0.0) time = 0.0;

        opData.setCountedArea(roundTo001(countedArea));
        opData.setDyeWeight(roundTo001(dyeWeight));
        opData.setPaintTime(roundTo001(time));
        return opData;
    }

}
