package ru.wert.normic.controllers.locksmith.counters;

import lombok.Getter;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.opLocksmith.OpDrillingByMarking;
import ru.wert.normic.enums.EMeasure;
import ru.wert.normic.interfaces.NormCounter;

import java.util.NoSuchElementException;

import static ru.wert.normic.AppStatics.roundTo001;

public class OpDrillingByMarkingCounter implements NormCounter {

    int[] depths = new int[]{                       5,    27,   10,   12,   15,   20,   25,   30,   40};
    private final double delta = 0.01;
    //https://sudact.ru/law/obshchemashinostroitelnye-normativy-vremeni-na-slesarno-instrumentalnye-raboty-vypolniaemye/normativnaia-chast/razdel-ii/karta-37/list-1_35/
    public enum EDrillingOnRadial {
        DRILLING_ON_RADIAL_D3 (3,   new double[]{0.32, 0.38, 0.47, 0.52, 0.58, 0.68, 0.77, 0.86, 1.00}),
        DRILLING_ON_RADIAL_D4 (4,   new double[]{0.28, 0.34, 0.41, 0.46, 0.52, 0.61, 0.69, 0.76, 0.89}),
        DRILLING_ON_RADIAL_D5 (5,   new double[]{0.29, 0.35, 0.43, 0.48, 0.54, 0.63, 0.71, 0.79, 0.92}),
        DRILLING_ON_RADIAL_D6 (6,   new double[]{0.32, 0.38, 0.46, 0.51, 0.58, 0.68, 0.77, 0.85, 1.00}),
        DRILLING_ON_RADIAL_D8 (8,   new double[]{0.36, 0.43, 0.52, 0.58, 0.65, 0.77, 0.87, 0.96, 1.10}),
        DRILLING_ON_RADIAL_D10(10,  new double[]{0.39, 0.47, 0.57, 0.63, 0.72, 0.84, 0.95, 1.05, 1.25}),
        DRILLING_ON_RADIAL_D12(12,  new double[]{0.42, 0.54, 0.62, 0.68, 0.77, 0.90, 1.00, 1.15, 1.30}),
        DRILLING_ON_RADIAL_D16(16,  new double[]{0.47, 0.57, 0.69, 0.77, 0.87, 1.00, 1.15, 1.25, 1.50}),
        DRILLING_ON_RADIAL_D20(20,  new double[]{0.52, 0.63, 0.76, 0.84, 0.95, 1.10, 1.25, 1.40, 1.65}),
        DRILLING_ON_RADIAL_D25(25,  new double[]{0.57, 0.69, 0.83, 0.92, 1.05, 1.20, 1.40, 1.55, 1.80});


        @Getter
        public int diameter;
        @Getter double[] times;
        EDrillingOnRadial(int diameter, double[] times){
            this.diameter = diameter;
            this.times = times;}
    }

    public OpData count(OpData data){
        OpDrillingByMarking opData = (OpDrillingByMarking)data;

        int holes = opData.getHoles();
        int depth = opData.getDepth();
        int length = opData.getLength();
        int diameter = opData.getDiameter();

        if(holes == 0 || depth == 0|| length == 0 || diameter == 0){
            opData.setMechTime(0.0);
            return opData;
        }

        //######################################################

        double time = findTimeForMarking(length) * (holes + 1) +    //Время разметки
                holes * findTimeForDrilling(depth, diameter);          //Время сверления

        opData.setMechTime(roundTo001(time));
        return opData;
    }

    /**
     * Время сверления
     */
    private Double findTimeForDrilling(int depth, int diameter) {
        int prevDepth = 0;
        for (int i = 0; i < depths.length; i++) {
            if (depth >= prevDepth && depth <= depths[i]) {
                int prevDiam = 0;
                for (EDrillingOnRadial eDiam :EDrillingOnRadial.values()) {
                    if (diameter >= prevDiam && diameter <= eDiam.getDiameter()) {
                        if (i < eDiam.getTimes().length)
                            return eDiam.getTimes()[i];
                        else
                            //Возвращаем самое последнее значение в массиве
                            return eDiam.getTimes()[eDiam.getTimes().length - 1];
                    }
                    prevDiam = eDiam.getDiameter();
                }
            }
            prevDepth = depths[i];
        }

        throw new NoSuchElementException("Ошибка при определении значения нормы времени в таблице");
    }

    /**
     * Время разметки
     */
    private Double findTimeForMarking(int length){
        EMeasure lastMeasure = EMeasure.values()[EMeasure.values().length-1];
        if(length > lastMeasure.getLength())
            return lastMeasure.getTime();

        int prevL = 0;
        for (EMeasure d : EMeasure.values()) {
            if (length >= prevL && length <= d.getLength())
                return d.getTime();

            prevL = d.getLength();
        }

        throw new NoSuchElementException("Ошибка при определении значения нормы времени в таблице");
    }

}
