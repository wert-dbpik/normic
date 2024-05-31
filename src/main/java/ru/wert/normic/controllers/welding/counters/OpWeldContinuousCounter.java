package ru.wert.normic.controllers.welding.counters;

import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.opList.OpCutting;
import ru.wert.normic.entities.ops.opWelding.OpWeldContinuous;
import ru.wert.normic.interfaces.NormCounter;

import static ru.wert.normic.AppStatics.roundTo001;
import static ru.wert.normic.controllers.AbstractOpPlate.MM_TO_M;
import static ru.wert.normic.settings.NormConstants.WELDING_SPEED;

public class OpWeldContinuousCounter implements NormCounter {

    public OpData count(OpData data){
        OpWeldContinuous opData = (OpWeldContinuous)data;

        double assemblingTime = opData.getPartBigness().getTime(); //Время сборки свариваемого узла
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

        double strippingTime;
        if(stripping) {
            //Время на зачистку, мин
            if (sumWeldLength < 100) strippingTime = 0.5;
            else if (sumWeldLength >= 100 && sumWeldLength < 500) strippingTime = 1.8;
            else if (sumWeldLength >= 500 && sumWeldLength < 1000) strippingTime = 3.22;
            else strippingTime = sumWeldLength * MM_TO_M * 3.22;
        } else
            strippingTime = 0.0;


        double time;
        time =  men * (sumWeldLength * MM_TO_M * WELDING_SPEED + assemblingTime) + strippingTime;   //мин
        if(sumWeldLength == 0.0) time = 0.0;

        opData.setMechTime(roundTo001(time));
        return opData;
    }
}
