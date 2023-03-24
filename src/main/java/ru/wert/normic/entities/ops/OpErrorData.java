package ru.wert.normic.entities.ops;


import lombok.Getter;
import lombok.Setter;
import ru.wert.normic.enums.ENormType;
import ru.wert.normic.enums.EOpType;
import ru.wert.normic.interfaces.IOpWithOperations;

import java.util.ArrayList;
import java.util.List;

import static ru.wert.normic.enums.EOpType.findOpTypeByName;

@Getter
@Setter
public class OpErrorData extends OpData {

    private String name = null;
    private Integer width = 0;
    private Integer depth = 0;
    private Integer height = 0;
    private double weight = 0.0;
    private double area = 0.0;

    private String errorOpData; //Операция в которой произошла ошибка



    public OpErrorData(String errorOpData) {
        super.normType = ENormType.NORM_PACKING;
        super.opType = EOpType.ERROR_OP_DATA;

        this.errorOpData = errorOpData;
    }

    @Override
    public String toString() {
        return "ошибка конвертации операции : " + findOpTypeByName(errorOpData).getOpName();
    }
}
