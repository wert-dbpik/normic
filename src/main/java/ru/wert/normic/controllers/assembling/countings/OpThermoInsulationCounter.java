package ru.wert.normic.controllers.assembling.countings;

import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.opAssembling.OpLevelingSealer;
import ru.wert.normic.entities.ops.opAssembling.OpThermoInsulation;
import ru.wert.normic.enums.EMaterialMeasurement;
import ru.wert.normic.interfaces.NormCounter;

import static ru.wert.normic.controllers.AbstractOpPlate.MM2_TO_M2;
import static ru.wert.normic.controllers.AbstractOpPlate.MM_TO_M;
import static ru.wert.normic.settings.NormConstants.*;

public class OpThermoInsulationCounter implements NormCounter {


    @Override
    public OpThermoInsulation count(OpData data) {
        OpThermoInsulation opData = (OpThermoInsulation)data;

        int thickness = opData.getThickness();

        int height = opData.getHeight();
        int width = opData.getWidth();
        int depth = opData.getDepth();

        boolean countFront = opData.getFront();
        boolean countBack = opData.getBack();

        double front = width * height * MM2_TO_M2; //площадь фронта, м2
        double side = depth * height * MM2_TO_M2; //площадь боковой стороны, м2
        double top = depth * width * MM2_TO_M2; //площадь верха, м2

        double square = 2*top + 2*side + (countFront ? front : 0.0) + (countBack ? front : 0.0);
        double volume = square * (thickness * MM_TO_M); //Расход материала

        double plusRatio = opData.getPlusRatio();

        //#########################################################################################

        EMaterialMeasurement measurement = opData.getMeasurement();
        opData.setOutlay(measurement.equals(EMaterialMeasurement.M2) ? square * plusRatio : volume * plusRatio);

        double time =  square * INSULATION_SPEED;  //мин
        opData.setAssmTime(time);


        return opData;
    }
}
