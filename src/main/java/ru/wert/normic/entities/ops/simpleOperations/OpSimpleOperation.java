package ru.wert.normic.entities.ops.simpleOperations;

import lombok.Getter;
import lombok.Setter;
import ru.wert.normic.entities.db_connection.material.Material;
import ru.wert.normic.entities.db_connection.simpleOperations.SimpleOperation;
import ru.wert.normic.entities.db_connection.simpleOperations.SimpleOperationServiceImpl;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.enums.EOpType;

import static java.lang.String.format;

@Getter
@Setter
public class OpSimpleOperation extends OpData {

    transient SimpleOperation operationPrototype;

    private String name;
    private Material material;
    private Long simpleOtherOpId; //id операции из БД
    private int paramA = 0; //парамет А
    private int paramB = 0; //парамет B
    private int paramC = 0; //парамет C
    private int num = 1; //Количество
    private boolean inputCounted = false; //Ручной ввод параметров A, B и C
    private double countedAmount = 0.0; //Окончательно изготовлено
    private double manualAmount = 0.0; //Окончательно изготовлено

    public OpSimpleOperation(SimpleOperation operationPrototype) {
        this.operationPrototype = operationPrototype;
        simpleOtherOpId = operationPrototype.getId();

        super.normType = operationPrototype.getNormType();
        super.jobType = operationPrototype.getJobType();
        super.opType = EOpType.SIMPLE_OPERATION;
    }

    public SimpleOperation getOperationPrototype(){
        if(operationPrototype == null)
            operationPrototype = SimpleOperationServiceImpl.getInstance().findById(simpleOtherOpId);
        return operationPrototype;

    }

    @Override
    public String toString() {

        return format("Изготовлено = %f.3 %s", countedAmount, operationPrototype.getMeasurement());
    }


}
