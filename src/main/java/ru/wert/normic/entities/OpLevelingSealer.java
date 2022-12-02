package ru.wert.normic.entities;

import lombok.Getter;
import lombok.Setter;
import ru.wert.normic.enums.ENormType;
import ru.wert.normic.enums.EOpType;
import ru.wert.normic.enums.ESealersWidth;


@Getter
@Setter
public class OpLevelingSealer extends OpData {

    private ESealersWidth sealersWidth = ESealersWidth.W10; //Ширина уплотнителя
    private Integer paramA = 0; //Размер А
    private Integer paramB = 0;//Размер Б

    public OpLevelingSealer() {
        super.normType = ENormType.NORM_ASSEMBLING;
        super.opType = EOpType.LEVELING_SEALER;
    }
}
