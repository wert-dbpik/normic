package ru.wert.normic.controllers.simplOperations.counters;

import ru.wert.normic.entities.db_connection.simpleOperations.SimpleOperation;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.simpleOperations.OpSimpleOperation;
import ru.wert.normic.enums.ENormType;
import ru.wert.normic.enums.EPieceMeasurement;
import ru.wert.normic.interfaces.NormCounter;

import static ru.wert.normic.AppStatics.roundTo001;
import static ru.wert.normic.controllers.AbstractOpPlate.*;

public class OpSimpleOperationsCounter implements NormCounter{

    public OpData count(OpData data){
        OpSimpleOperation opData = (OpSimpleOperation)data;
        SimpleOperation operation = opData.getOperationPrototype();
        EPieceMeasurement measurement = operation.getMeasurement();

        int paramA = opData.getParamA();
        int paramB = opData.getParamB();
        int paramC = opData.getParamC();
        int number = opData.getNum();


        double normTime = operation.getNorm();
        ENormType eNormType = operation.getNormType();

        double amount = 0.0;

        if (opData.isInputCounted()) {
            switch (measurement) {
                case METER:
                    if(paramA == 0 || paramB == 0)
                        opData.setCountedAmount(Math.max(paramA, paramB) * MM_TO_M);
                    else
                        opData.setCountedAmount(2 * (paramA + paramB)  * MM_TO_M);
                    break;
                case SQUARE_METER:
                    opData.setCountedAmount(paramA * paramB * MM2_TO_M2);
                    break;
                case CUBE_METER:
                    opData.setCountedAmount(paramA * paramB * paramC * MM3_TO_M3);
                    break;
            }
            amount = opData.getCountedAmount();
        }
        else
            amount = opData.getManualAmount();

        //#############################################################################

        double time = amount * normTime * number;

        switch(eNormType){
            case NORM_MECHANICAL: opData.setMechTime(roundTo001(time)); break;
            case NORM_PAINTING: opData.setPaintTime(roundTo001(time)); break;
            case NORM_ASSEMBLING: opData.setAssmTime(roundTo001(time)); break;
            case NORM_PACKING: opData.setPackTime(roundTo001(time)); break;
        }

        return opData;
    }

}
