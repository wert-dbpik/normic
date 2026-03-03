package ru.wert.normic.controllers._plates.welding.counters;

import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.opWelding.OpWeldContinuousNew;
import ru.wert.normic.interfaces.NormCounter;

import static ru.wert.normic.AppStatics.roundTo001;
import static ru.wert.normic.controllers._plates.AbstractOpPlate.MM_TO_M;
import static ru.wert.normic.settings.NormConstants.WELDING_SPEED;

public class OpWeldContinuousCounterNew implements NormCounter {

    public OpData count(OpData data){
        OpWeldContinuousNew opData = (OpWeldContinuousNew)data;

        boolean preEnterSeams = opData.isPreEnterSeams();
        boolean stripping = opData.isStripping();
        int step = opData.getStep();
        int seams = opData.getSeams();
        int connectionLength = opData.getConnectionLength();
        int seamLength = opData.getSeamLength();
        int men = opData.getMen();

        if (!preEnterSeams && step == 0) {//Деление на ноль
            opData.setMechTime(0.0);
            return opData;
        }

        //######################################################

        int sumWeldLength = preEnterSeams ?
                seams * seamLength :
                connectionLength / step * seamLength;

        double stripOneSeamTime; //Время зачистки одного шва
        if(stripping) {
            //Время на зачистку, мин
            if (seamLength < 100) stripOneSeamTime = 0.5;
            else if (seamLength >= 100 && seamLength < 500) stripOneSeamTime = 1.8;
            else if (seamLength >= 500 && seamLength < 1000) stripOneSeamTime = 3.22;
            else stripOneSeamTime = seamLength * MM_TO_M * 3.22;
        } else
            stripOneSeamTime = 0.0;


        double time;
        double weldingTime = men * (sumWeldLength * MM_TO_M * WELDING_SPEED);
        double strippingTime = stripOneSeamTime * seams;
        time =  weldingTime + strippingTime;   //мин
        if(sumWeldLength == 0.0) time = 0.0;

        opData.setMechTime(roundTo001(time));
        opData.setLocksmithTime(roundTo001(strippingTime));

        return opData;
    }
}
