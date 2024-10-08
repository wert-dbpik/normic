package ru.wert.normic.controllers.paint.counters;

import javafx.application.Platform;
import ru.wert.normic.decoration.warnings.Warning2;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.opPaint.OpPaintAssm;
import ru.wert.normic.entities.ops.single.OpAssm;
import ru.wert.normic.entities.ops.single.OpDetail;
import ru.wert.normic.enums.EColor;
import ru.wert.normic.enums.EOpType;
import ru.wert.normic.interfaces.IOpWithOperations;
import ru.wert.normic.interfaces.NormCounter;
import ru.wert.normic.interfaces.Paintable;

import java.util.ArrayList;
import java.util.List;

import static ru.wert.normic.AppStatics.MAIN_CONTROLLER;
import static ru.wert.normic.AppStatics.roundTo001;
import static ru.wert.normic.settings.NormConstants.*;

public class OpPaintAssmCounter implements NormCounter {

    private OpPaintAssm opData;
    private OpAssm assm;
    private double finalPaintedArea;
    private boolean useCalculatedArea;
    private double kArea;

    private double countedArea; //Переменная для расчета площади покрытия
    //Список операций с дублирующей покраской, создан чтобы сообщения не доставали пользователя
    List<IOpWithOperations> opsWithDoublePainting = new ArrayList<>();

    public OpData count(OpData data) {
        opData = (OpPaintAssm) data;

        assm = opData.getAssm();
        /*
        assm = null при загрузке изделия из файла. Поле assm класса OpPaintAssm является transient и не хранит сборку изначально.
        поэтому при пересчете лучше вернуть opData неизменным
         */
        if(assm == null) return opData;

        EColor color = opData.getColor(); //Цвет краски
        int along = opData.getAlong(); //Параметр А вдоль штанги
        int across = opData.getAcross(); //Параметр B поперек штанги
        double area = opData.getArea(); //Площадь покрытия введенная вручную
        double pantingSpeed = opData.getAssmType().getSpeed();// Скорость нанесения покрытия
        boolean twoSides = opData.isTwoSides(); //Красить с двух сторон

        this.useCalculatedArea = opData.isCalculatedArea(); //Использовать расчетную площадь окрашивания
        this.kArea = twoSides ? 1.0 : 0.5;
        //###########################################################################

        countedArea = 0.0;
        double countedArea = countCalculatedArea(assm);
        finalPaintedArea = useCalculatedArea ? countedArea * kArea : area;


        double dyeWeight = color.getConsumption() * 0.001 * finalPaintedArea;

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
        time = HANGING_TIME//Время навешивания
                + finalPaintedArea * WINDING_MOVING_SPEED //Время подготовки к окрашиванию
                + finalPaintedArea * pantingSpeed //Время нанесения покрытия
                + 40.0 / bakeBars / partsOnBar;  //Время полимеризации
        if (finalPaintedArea == 0.0) time = 0.0;

        opData.setCountedArea(roundTo001(countedArea));
        opData.setDyeWeight(roundTo001(dyeWeight));
        opData.setPaintTime(roundTo001(time));
        return opData;
    }

    /**
     * Расчитывает площадь всех входящих в сборку других деталей и сборок.
     * Нулевое значение assm при первоначальной загрузке из файла уже отсечено
     */
    private double countCalculatedArea(IOpWithOperations assm) {
        List<OpData> ops = assm.getOperations();
        for (OpData op : ops) {
            if (op instanceof OpAssm) {
                checkIfDoublePainting((IOpWithOperations) op);
                countCalculatedArea((IOpWithOperations) op);
            }
            if (op instanceof OpDetail) {
                checkIfDoublePainting((IOpWithOperations) op);
                countedArea += ((OpDetail) op).getArea() * op.getQuantity();//количество деталей в сборке
            }
        }
        return countedArea;
    }

    /**
     * Проверяет входящие сборки и детали на дублирующее окрашивание
     * Выносит предупреждение и предлагает удалить
     *
     * @param opWithOperations
     */
    private void checkIfDoublePainting(IOpWithOperations opWithOperations) {
        for (OpData op : opWithOperations.getOperations()) {
            //Маркируем по пути все окрашиваемые узлы, чтобы контролировать их изменение
            if (op instanceof Paintable) ((Paintable) op).setPainter(assm);
            if (op.getOpType().equals(EOpType.PAINTING) || op.getOpType().equals(EOpType.PAINT_ASSM)) {
                if (!opsWithDoublePainting.contains(opWithOperations)) {
                    opsWithDoublePainting.add(opWithOperations);
                    Platform.runLater(() -> {
                        boolean res = Warning2.create(
                                null,
                                "Внимание!",
                                String.format("'%s' содержит дублирующую покраску.", opWithOperations.getName()),
                                "Удалить повторное окрашивание?");
                        if (res) {
                            opWithOperations.getOperations().remove(op);
                            ((OpData) opWithOperations).setPaintTime(0f);
                            //TODO : удалить плашку также, если она доступна
                            opsWithDoublePainting.remove(opWithOperations);

                            MAIN_CONTROLLER.recountPaintingMainOpData();
                        }
                    });
                }
            }
        }
    }


}
