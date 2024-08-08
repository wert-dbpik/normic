package ru.wert.normic.controllers.turning.counters;

import lombok.Getter;
import ru.wert.normic.dataBaseEntities.ops.OpData;
import ru.wert.normic.dataBaseEntities.ops.opTurning.OpLatheCutOff;
import ru.wert.normic.interfaces.NormCounter;

import java.util.NoSuchElementException;

import static ru.wert.normic.AppStatics.roundTo001;

public class OpLatheCutOffCounter implements NormCounter {

    private final double maxDiameter = 100.0;
    private final double maxThickness = 90.0;

    /**
     * Зависимость времени отрезания (мин) от диаметра отрезаемого прутка (мм)
     */
    enum ECutSolidDiameters { //page 123 (Р6М5)
        CUT_SOLID_D10(10, 0.9),
        CUT_SOLID_D20(20, 1.0),
        CUT_SOLID_D30(30, 1.5),
        CUT_SOLID_D40(40, 1.8),
        CUT_SOLID_D60(60, 2.7),
        CUT_SOLID_D80(80, 4.3),
        CUT_SOLID_D90(90, 5.0),
        CUT_SOLID_D100(100, 6.4);

        @Getter
        int diam;
        @Getter double time;
        ECutSolidDiameters(int diam,  double time){
            this.diam = diam;
            this.time = time;}
    }

    /**
     * Зависимость времени отрезания (мин) от толщины стенки трубы (мм)
     */
    enum ECutPipeThicknesses { //page 124 (Р6М5)
        CUT_PIPE_T05(5, 0.9),
        CUT_PIPE_T10(10, 1.0),
        CUT_PIPE_T20(20, 1.3),
        CUT_PIPE_T30(30, 2.5),
        CUT_PIPE_T40(40, 3.5),
        CUT_PIPE_T50(50, 4.3),
        CUT_PIPE_T60(60, 5.5),
        CUT_PIPE_T70(70, 8.5),
        CUT_PIPE_T80(80, 9.5),
        CUT_PIPE_T90(90, 13.5);

        @Getter int thickness;
        @Getter double time;

        ECutPipeThicknesses(int thickness, double time){
            this.thickness = thickness;
            this.time = time;}
    }

    public OpData count(OpData data){
        OpLatheCutOff opData = (OpLatheCutOff)data;

        double diameter = opData.getMaterial().getParamS(); //Диаметр заготовки
        boolean cutOffSolid = opData.getCutOffSolid(); //отрезание детали сплошного сечения
        double thickness = opData.getThickness(); //Глубина  точения

        //Значения должны быть не больше допустимых
        diameter = Math.min(diameter, maxDiameter);
        thickness = Math.min(thickness, maxThickness);

        //##################################################

        double time = findTime(cutOffSolid, diameter, thickness);

        opData.setMechTime(roundTo001(time));
        return opData;
    }

    private Double findTime(boolean cutOffSolid, double diameter, double thickness){
        if(cutOffSolid){
            if(diameter == 0.0) return 0.0;
            int prevD = 0;
            for(ECutSolidDiameters d : ECutSolidDiameters.values()){
                if(diameter > prevD && diameter <= d.getDiam())
                    return d.getTime();
                prevD = d.getDiam();
            }
        } else {
            if(thickness == 0.0) return 0.0;
            int prevT = 0;
            for(ECutPipeThicknesses d : ECutPipeThicknesses.values()){
                if(thickness > prevT && thickness <= d.getThickness())
                    return d.getTime();
                prevT = d.getThickness();
            }
        }

        throw new NoSuchElementException("Ошибка при определении значения нормы времени в таблице");
    }
}
