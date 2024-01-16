package ru.wert.normic.controllers.turning.counters;

import lombok.Getter;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.opList.OpCutting;
import ru.wert.normic.entities.ops.opTurning.OpLatheCutGroove;
import ru.wert.normic.interfaces.NormCounter;

import java.util.NoSuchElementException;

public class OpLatheCutGrooveCounter implements NormCounter {

    private final int maxDepth = 35; //Максимальная глубина канавки

    /**
     * Зависимость времени обработки (мин) от глубины канавки (мм)
     * page 129 (Р6М5)
     */
    enum ECutGroove {
        CUT_GROOVE_T2(2, 0.8),
        CUT_SOLID_D20(5, 0.9),
        CUT_SOLID_D30(10, 1.0),
        CUT_SOLID_D40(15, 1.4),
        CUT_SOLID_D60(20, 1.6),
        CUT_SOLID_D80(25, 5.5),
        CUT_SOLID_D90(30, 6.5),
        CUT_SOLID_D100(35, 8.5);

        @Getter
        int depth;
        @Getter double time;
        ECutGroove(int depth, double time){
            this.depth = depth;
            this.time = time;}
    }

    /**
     * Перебирает строки ECutGroove
     */
    private Double findTime(double depth){
        if(depth == 0.0) return 0.0;
        int prevD = 0; //Начальное значение при переборе
        for(ECutGroove d : ECutGroove.values()){
            if(depth >= prevD && depth <= d.getDepth())
                return d.getTime();
            prevD = d.getDepth();
        }


        throw new NoSuchElementException("Ошибка при определении значения нормы времени в таблице");
    }

    public OpData count(OpData data){
        OpLatheCutGroove opData = (OpLatheCutGroove)data;

        double depth = opData.getDepth(); //Глубина  точения

        //Значения должны быть не больше допустимых
        depth = Math.min(depth, maxDepth);
        //#######################################################

        double time = findTime(depth);

        opData.setMechTime(time);
        return opData;
    }
}
