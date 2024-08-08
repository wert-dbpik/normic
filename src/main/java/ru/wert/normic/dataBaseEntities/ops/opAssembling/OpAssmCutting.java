package ru.wert.normic.dataBaseEntities.ops.opAssembling;

import lombok.Getter;
import lombok.Setter;
import ru.wert.normic.dataBaseEntities.ops.OpData;
import ru.wert.normic.enums.ENormType;
import ru.wert.normic.enums.EOpType;

/**
 * СБОРКА РАСКРОЙНЫХ МАТЕРИАЛОВ
 */
@Getter
@Setter
public class OpAssmCutting extends OpData {


    private Double sealer = 0.0;
    private Double selfAdhSealer = 0.0;
    private Double insulation = 0.0;

    public OpAssmCutting() {
        super.normType = ENormType.NORM_ASSEMBLING;
        super.opType = EOpType.ASSM_CUTTINGS;
    }

    @Override
    public String toString() {
        return "уплотнитель = " + sealer + " м." +
                ", уплотнитель самоклеющийся = " + selfAdhSealer + " м." +
                ", утеплитель = " + insulation + " м.кв.";
    }
}
