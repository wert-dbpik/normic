package ru.wert.normik.entities;

import lombok.Getter;
import lombok.Setter;
import ru.wert.normik.enums.ENormType;
import ru.wert.normik.enums.EOpType;

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
