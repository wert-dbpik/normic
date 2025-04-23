package ru.wert.normic.entities.ops.electrical;

import lombok.Getter;
import lombok.Setter;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.enums.ENormType;
import ru.wert.normic.enums.EOpType;

/**
 * ИЗОЛЯЦИЯ ПРОВОДОВ ТЕРМОУСАДОЧНОЙ ТРУБКОЙ 10-30 ММ
 */
@Getter
@Setter
public class OpIsolateWithThermTube30 extends OpData {

    private String name = "";
    private int pins = 0;


    public OpIsolateWithThermTube30() {
        super.normType = ENormType.NORM_ELECTRICAL;
        super.opType = EOpType.EL_ISOLATE_WITH_THERM_TUBE30;
    }

    @Override
    public String toString() {
        return String.format("Мест - %s", pins);
    }
}
