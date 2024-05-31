package ru.wert.normic.controllers.turning.counters;

import lombok.Getter;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.opList.OpCutting;
import ru.wert.normic.entities.ops.opTurning.OpLatheDrilling;
import ru.wert.normic.entities.ops.opTurning.OpLatheThreading;
import ru.wert.normic.interfaces.NormCounter;

import java.util.NoSuchElementException;

import static ru.wert.normic.AppStatics.roundTo001;

public class OpLatheThreadingCounter implements NormCounter {

    private final int maxTurningDiameter = 30; //Максимальный диаметр свреления
    private final int maxTurningLength = 40; //Максимальная длина обработки

    private int[] lengths = new int[]{10, 15, 20, 25, 30, 40};

    enum EThreading {
        THREADING_D6(6, new double[]{0.95, 1.05, 1.20, 1.25, 1.35, 1.45}), //Плашкой стр155 (Р6М5)
        THREADING_D8(8, new double[]{0.95, 1.05, 1.20, 1.30, 1.40, 1.55}), //Плашкой стр155 (Р6М5)
        THREADING_D10(10, new double[]{1.00, 1.15, 1.25, 1.35, 1.45, 1.65}), //Плашкой стр155 (Р6М5)
        THREADING_D12(12, new double[]{1.05, 1.15, 1.30, 1.45, 1.55, 1.76}), //Плашкой стр155 (Р6М5)
        THREADING_D16(16, new double[]{1.05, 1.15, 1.45, 1.55, 1.65, 1.85}), //Плашкой стр155 (Р6М5)

        THREADING_D24(24, new double[]{2.20, 2.40, 2.60, 2.80, 3.00, 3.20}), //Резцом стр143 (Р6М5)
        THREADING_D30(30, new double[]{2.30, 2.50, 2.70, 2.90, 3.10, 3.30}); //Резцом стр143 (Р6М5)


        @Getter
        int diameter;
        @Getter
        double[] times;

        EThreading(int diameter, double[] times) {
            this.diameter = diameter;
            this.times = times;
        }

    }

    public OpData count(OpData data){
        OpLatheThreading opData = (OpLatheThreading)data;

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

    private Double findTime(double diameter, int length) {
        if(diameter == 0.0 || length == 0) return 0.0;
        int maxLength = lengths[lengths.length - 1];
        if (length > maxLength) {
            int prevD = 0;
            for (EThreading eDiam : EThreading.values()) {
                if (diameter >= prevD && diameter <= eDiam.getDiameter()) {
                    return eDiam.getTimes()[eDiam.getTimes().length - 1] + 0.02 * (length - maxLength);
                }
                prevD = eDiam.getDiameter();
            }
        } else {
            int prevL = 0;
            for (int i = 0; i < lengths.length; i++) {
                if (length >= prevL && length <= lengths[i]) {
                    int prevD = 0;
                    for (EThreading eDiam : EThreading.values()) {
                        if (diameter >= prevD && diameter <= eDiam.getDiameter()) {
                            return eDiam.getTimes()[i];
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


