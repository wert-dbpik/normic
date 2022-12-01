package ru.wert.normik.entities;

import lombok.Getter;
import lombok.Setter;
import ru.wert.normik.enums.ENormType;
import ru.wert.normik.enums.EOpType;

@Getter
@Setter
public class OpAssmNut extends OpData {

    private Integer screws = 0; //винты
    private Integer vshgs = 0; //ВШГ - винт-шайба-гайка
    private Integer rivets = 0; //заклепки
    private Integer rivetNuts = 0; //заклепочные гайки
    private Integer groundSets = 0;//заземление(гайка-шайба-этикетка)
    private Integer others = 0;//другой крепеж

    public OpAssmNut() {
        super.normType = ENormType.NORM_ASSEMBLING;
        super.opType = EOpType.ASSM_NUTS;
    }
}
