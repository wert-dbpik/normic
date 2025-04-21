package ru.wert.normic.entities.ops.electrical;

import lombok.Getter;
import lombok.Setter;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.enums.ENormType;
import ru.wert.normic.enums.EOpType;

/**
 * УКЛАДКА ЖГУТОВ
 */
@Getter
@Setter
public class OpFixOfCables extends OpData {

    private double length = 0.0;


    public OpFixOfCables() {
        super.normType = ENormType.NORM_ELECTRICAL;
        super.opType = EOpType.EL_FIX_OF_CABLES;
    }

    @Override
    public String toString() {
        return String.format("длина жгута - %s, м", length);
    }
}
