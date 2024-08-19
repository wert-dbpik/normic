package ru.wert.normic.controllers.turning.counters;

import lombok.Getter;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.opTurning.OpLatheMountDismount;
import ru.wert.normic.interfaces.NormCounter;

import java.util.NoSuchElementException;

import static ru.wert.normic.AppStatics.roundTo001;

public class OpLatheMountDismountCounter implements NormCounter {

    private final double maxWeight = 20; //Максимальный вес заготовки

    private double[] weights = new double[]{0.3,  1.0,  3.0,  5.0,  10.0,  20.0};
    /**
     * Время установки в зависимости от способа установки и массы заготовки (кг)
     */
    public enum ELatheHolders {
        CENTERS             (new double[]{0.17, 0.25, 0.35, 0.40, 0.50, 0.61}),
        HOLDER              (new double[]{0.58, 0.81, 1.05, 1.25, 1.50, 1.80}),
        HOLDER_AND_CENTER   (new double[]{0.33, 0.45, 0.61, 0.70, 0.84, 1.05});

        @Getter
        double[] times;

        ELatheHolders(double[] times){
            this.times = times;}
    }

    public OpData count(OpData data){
        OpLatheMountDismount opData = (OpLatheMountDismount)data;

        ELatheHolders holder = ELatheHolders.values()[opData.getHolder()];
        double weight = opData.getWeight(); //Масса заготовки

        weight = Math.min(weight, maxWeight);
        //##################################################################
        double time = findTime(holder, weight);

        opData.setMechTime(roundTo001(time));
        return opData;
    }

    private Double findTime(ELatheHolders holder, double weight){
        double countW = Math.min(weight, 20.0);
        double prevW = 0;
        for (int i = 0; i < weights.length; i++) {
            if (countW >= prevW && countW <= weights[i]) {
                return holder.getTimes()[i];
            }
            prevW = weights[i];
        }
        throw new NoSuchElementException("Ошибка при определении значения нормы времени в таблице");
    }
}
