package ru.wert.normic.controllers.turning.counters;

import lombok.Getter;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.opTurning.OpLatheDrilling;
import ru.wert.normic.interfaces.NormCounter;

import java.util.NoSuchElementException;

import static ru.wert.normic.AppStatics.roundTo001;

public class OpLatheDrillingCounter implements NormCounter{

    private final int maxTurningDiameter = 30; //Максимальный диаметр свреления
    private final int maxTurningLength = 100; //Максимальная длина обработки

    private int[] lengths = new int[]{10,   20,   40,   60,   80,   100};
    private double delta = 0.01;

    /**
     * Зависимость времени сверления (мин) от диаметра (мм) и длины отверстия (мм)
     */
    enum EDrilling { //page 174 (Р6М5)
        DRILLING_D6(6,   new double[]{0.30, 0.40, 0.70, 1.10, 1.40, 1.60}),
        DRILLING_D8(8,   new double[]{0.40, 0.40, 0.70, 1.20, 1.50, 1.70}),
        DRILLING_D10(10, new double[]{0.40, 0.50, 0.80, 1.20, 1.60, 1.70}),
        DRILLING_D12(12, new double[]{0.40, 0.50, 0.90, 1.30, 1.80, 1.90}),
        DRILLING_D16(16, new double[]{0.40, 0.50, 0.90, 1.30, 1.80, 1.90}),
        DRILLING_D20(20, new double[]{0.50, 0.70, 1.00, 1.50, 2.00, 2.40}),
        DRILLING_D25(5,  new double[]{0.60, 0.90, 1.20, 1.70, 2.20, 2.70}),
        DRILLING_D30(30, new double[]{0.80, 1.20, 1.60, 2.30, 3.00, 3.60});


        @Getter
        int diameter;
        @Getter double[] times;
        EDrilling(int diameter, double[] times){
            this.diameter = diameter;
            this.times = times;}
    }

    public OpData count(OpData data){
        OpLatheDrilling opData = (OpLatheDrilling)data;

        int turningDiameter = opData.getDiameter(); //Диаметр обработки
        int length = opData.getLength(); //Глубина  точения

        //Значения должны быть не больше допустимых
        turningDiameter = Math.min(turningDiameter, maxTurningDiameter);
        length = Math.min(length, maxTurningLength);
        //#####################################################

        double time = findTime(turningDiameter, length);

        opData.setMechTime(roundTo001(time));
        return opData;
    }

    private Double findTime(double diameter, int length){
        if(diameter == 0.0 || length == 0) return 0.0;
        int maxLength = lengths[lengths.length-1];
        if(length > maxLength){
            int prevD = 0;
            for (EDrilling eDrilling : EDrilling.values()) {
                if (diameter >= prevD && diameter <= eDrilling.getDiameter()) {
                    return eDrilling.getTimes()[eDrilling.getTimes().length-1] + delta * (length - maxLength);
                }
                prevD = eDrilling.getDiameter();
            }
        } else {
            int prevL = 0;
            for (int i = 0; i < lengths.length; i++) {
                if (length >= prevL && length <= lengths[i]) {
                    int prevD = 0;
                    for (EDrilling eDiam : EDrilling.values()) {
                        if (diameter >= prevD && diameter <= eDiam.getDiameter()) {
                            if (i < eDiam.getTimes().length)
                                return eDiam.getTimes()[i];
                            else
                                //Возвращаем самое последнее значение в массиве
                                return eDiam.getTimes()[eDiam.getTimes().length - 1];
                        }
                        prevD = eDiam.getDiameter();
                    }
                }
                prevL = lengths[i];
            }
        }

        throw new NoSuchElementException("Ошибка при определении значения нормы времени в таблице");
    }
}
