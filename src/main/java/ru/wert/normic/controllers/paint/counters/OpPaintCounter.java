package ru.wert.normic.controllers.paint.counters;

import ru.wert.normic.entities.db_connection.material.Material;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.opPaint.OpPaint;
import ru.wert.normic.enums.EColor;
import ru.wert.normic.enums.EMatType;
import ru.wert.normic.enums.EPaintingDifficulty;
import ru.wert.normic.interfaces.NormCounter;

import static ru.wert.normic.AppStatics.roundTo001;
import static ru.wert.normic.controllers.AbstractOpPlate.MM2_TO_M2;
import static ru.wert.normic.settings.NormConstants.*;

public class OpPaintCounter implements NormCounter {

    public OpData count(OpData data){
        OpPaint opData = (OpPaint)data;

        Material material = opData.getMaterial();
        EColor color = opData.getColor();
        int razvA = opData.getRazvA();
        int razvB = opData.getRazvB();
        boolean twoSides = opData.isTwoSides();
        int along = opData.getAlong();
        int across = opData.getAcross();
        int hangingTime = opData.getHangingTime();
        EPaintingDifficulty difficulty = opData.getDifficulty();

        double kArea; //Коэффициент показывающий двустороннюю окрашиваемость
        double coatArea; //Площадь покрытия
        double dyeWeight; //Расход краски

        //Определяем площадь покрытия
        if (material.getMatType().getName().equals(EMatType.LIST.getName())) {
            kArea = twoSides ? 1.0 : 0.5;
            coatArea = razvA * razvB * 2 * MM2_TO_M2 * kArea; //Площадь покрытия
        } else {
            //Масса погонного метра
            double diameter = material.getParamS();
            coatArea = 3.14 * diameter * (razvA + razvB) * MM2_TO_M2;
        }

        dyeWeight = (color.getConsumption() * 0.001) * coatArea;


        //##########################################################################

        final double HANGING_TIME = hangingTime / 60.0; //время навешивания, мин

        final int alongSize = Math.max(along, across) + DETAIL_DELTA;
        final int acrossSize = Math.min(along, across) + DETAIL_DELTA;

        //Количество штанг в сушилке
        int dryingBars;
        if (acrossSize < 99) dryingBars = 3;
        else if (acrossSize >= 100 && acrossSize <= 300) dryingBars = 2;
        else dryingBars = 1;

        int partsOnBar = 2500 / alongSize;

        //Количество штанг в печи
        int bakeBars;
        if (acrossSize < 49) bakeBars = 6;
        else if (acrossSize >= 50 && acrossSize <= 99) bakeBars = 5;
        else if (acrossSize >= 100 && acrossSize <= 199) bakeBars = 4;
        else if (acrossSize >= 200 && acrossSize <= 299) bakeBars = 3;
        else if (acrossSize >= 300 && acrossSize <= 399) bakeBars = 2;
        else bakeBars = 1;

/*        //Внутренняя ширина печи 1560 мм
        int bakeBars;
        if (acrossSize < 259) bakeBars = 6;
        else if (acrossSize >= 260 && acrossSize <= 311) bakeBars = 5;
        else if (acrossSize >= 312 && acrossSize <= 389) bakeBars = 4;
        else if (acrossSize >= 390 && acrossSize <= 519) bakeBars = 3;
        else if (acrossSize >= 520 && acrossSize <= 779) bakeBars = 2;
        else bakeBars = 1;*/

        //Для цветной краски максимальное количество штанг 4
        if(bakeBars > 4 && !color.getRal().contains("7035")) bakeBars = 4;

        double time;
        time = HANGING_TIME //Время навешивания
                + ((WASHING / 60.0) + (WINDING / 60.0) + (DRYING / 60.0) / dryingBars) / partsOnBar //Время подготовки к окрашиванию
                + Math.pow(2 * coatArea, 0.7) * difficulty.getDifficultyRatio() //Время нанесения покрытия
                + 40.0 / bakeBars / partsOnBar;  //Время полимеризации

        if (coatArea == 0.0) time = 0.0;

        //Сохраняем все полученные значения
        opData.setArea(roundTo001(coatArea));
        opData.setDyeWeight(roundTo001(dyeWeight));
        opData.setPaintTime(roundTo001(time));
        return opData;
    }
}
