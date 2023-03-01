package ru.wert.normic.entities.opAssm;

import lombok.Getter;
import lombok.Setter;
import ru.wert.normic.entities.OpData;
import ru.wert.normic.enums.ENormType;
import ru.wert.normic.enums.EOpType;

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
}
