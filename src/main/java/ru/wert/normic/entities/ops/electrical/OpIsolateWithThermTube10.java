package ru.wert.normic.entities.ops.electrical;

import lombok.Getter;
import lombok.Setter;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.enums.ENormType;
import ru.wert.normic.enums.EOpType;

/**
 * ИЗОЛЯЦИЯ ПРОВОДОВ ТЕРМОУСАДОЧНОЙ ТРУБКОЙ 2-10 ММ
 */
@Getter
@Setter
public class OpIsolateWithThermTube10 extends OpData {

    private String name = "";
    private int pins = 1;


    public OpIsolateWithThermTube10() {
        super.normType = ENormType.NORM_ELECTRICAL;
        super.opType = EOpType.EL_ISOLATE_WITH_THERM_TUBE10;
    }

    @Override
    public String toString() {
        return String.format("Мест - %s", pins);
    }
}
