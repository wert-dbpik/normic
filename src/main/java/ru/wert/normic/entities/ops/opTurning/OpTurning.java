package ru.wert.normic.entities.ops.opTurning;

import lombok.Getter;
import lombok.Setter;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.enums.ENormType;
import ru.wert.normic.enums.EOpType;

/**
 * ТОЧЕНИЕ ИЛИ РАСТАЧИВАНИЕ НА ТОКАРНОМ СТАНКЕ
 */
@Getter
@Setter
public class OpTurning extends OpData {

    private Integer length = 0; //длина точения
    private Integer passages = 1; //количество токарных проходов


    public OpTurning() {
        super.normType = ENormType.NORM_MECHANICAL;
        super.opType = EOpType.TURNING;
    }
}
