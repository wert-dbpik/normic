package ru.wert.normic.controllers.turning.counters;

import lombok.Getter;
import ru.wert.normic.entities.ops.opTurning.OpLatheDrilling;
import ru.wert.normic.entities.ops.opTurning.OpLatheRolling;

import java.util.NoSuchElementException;

public class OpLatheRollingCounter {

    private static final int maxTurningDiameter = 150; //Максимальный диаметр свреления
    private static final int maxTurningLength = 75; //Максимальная длина обработки

    private static int[] lengths = new int[]{            10,   20,   30,   40,   50,   75};

    enum ERolling { //page 174 (Р6М5)
        ROLLING_D20(20,0.012, new double[]{0.70, 1.90, 2.40, 2.60, 2.70, 3.00}),
        ROLLING_D30(30,0.016, new double[]{0.80, 2.00, 2.60, 2.90, 3.10, 3.50}),
        ROLLING_D40(40,0.024, new double[]{0.90, 2.10, 2.90, 3.20, 3.50, 4.10}),
        ROLLING_D50(50,0.052, new double[]{2.00, 2.40, 3.30, 3.70, 4.20, 5.50}),
        ROLLING_D75(75,0.08,  new double[]{2.10, 2.70, 3.70, 4.30, 5.00, 7.00}),
        ROLLING_D100(100,0.08,new double[]{2.60, 3.00, 4.20, 5.00, 6.00, 8.00}),
        ROLLING_D125(125,0.1, new double[]{2.70, 3.30, 4.70, 5.50, 6.50, 9.00}),
        ROLLING_D150(150,0.12,new double[]{2.80, 3.70, 5.50, 6.50, 8.00, 11.0});


        @Getter int diameter;
        @Getter double delta;
        @Getter double[] times;
        ERolling(int diameter, double delta, double[] times){
            this.diameter = diameter;
            this.delta = delta;
            this.times = times;}
    }

    public static OpLatheRolling count(OpLatheRolling opData){
        int turningDiameter = opData.getDiameter(); //Диаметр накатки
        int length = opData.getLength(); //Длина  накатки

        //Значения должны быть не больше допустимых
        turningDiameter = Math.min(turningDiameter, maxTurningDiameter);
        length = Math.min(length, maxTurningLength);
        //#####################################################

        double time = findTime(turningDiameter, length);

        opData.setMechTime(time);
        return opData;
    }

    private static Double findTime(double diameter, int length){
        if(diameter == 0.0 || length == 0) return 0.0;
        int maxLength = lengths[lengths.length-1];
        if(length > maxLength){
            int prevD = 0;
            for (ERolling eRolling : ERolling.values()) {
                if (diameter >= prevD && diameter <= eRolling.getDiameter()) {
                    return eRolling.getTimes()[eRolling.getTimes().length-1] + eRolling.getDelta() * (length - maxLength);
                }
                prevD = eRolling.getDiameter();
            }
        } else {
            int prevL = 0;
            for (int i = 0; i < lengths.length; i++) {
                if (length >= prevL && length <= lengths[i]) {
                    int prevD = 0;
                    for (ERolling eDiam : ERolling.values()) {
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
