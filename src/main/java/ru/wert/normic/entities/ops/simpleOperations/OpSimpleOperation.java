package ru.wert.normic.entities.ops.simpleOperations;

import lombok.Getter;
import lombok.Setter;
import ru.wert.normic.entities.db_connection.material.Material;
import ru.wert.normic.entities.db_connection.simpleOperations.SimpleOperation;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.enums.EOpType;

import static java.lang.String.format;

@Getter
@Setter
public class OpSimpleOperation extends OpData {

    transient SimpleOperation operation;

    private Material material;
    private Long simpleOtherOpId; //id операции из БД
    private int paramA = 0; //парамет А
    private int paramB = 0; //парамет B
    private int paramC = 0; //парамет C
    private int num = 1; //Количество
    private boolean inputCounted = true; //Ручной ввод параметров A, B и C
    private double countedAmount = 0.0; //Окончательно изготовлено
    private double manualAmount = 0.0; //Окончательно изготовлено

    public OpSimpleOperation(SimpleOperation operation) {
        this.operation = operation;
        simpleOtherOpId = operation.getId();

        super.normType = operation.getNormType();
        super.jobType = operation.getJobType();
        super.opType = EOpType.SIMPLE_OPERATION;
    }

    @Override
    public String toString() {

        return format("Изготовлено = %f.3 %s", countedAmount, operation.getMeasurement());
    }
}
