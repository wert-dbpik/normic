package ru.wert.normic.entities.ops.opAssembling;

import lombok.Getter;
import lombok.Setter;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.enums.ENormType;
import ru.wert.normic.enums.EOpType;
import ru.wert.normic.enums.ESealersWidth;

/**
 * НАЛИВКА УПЛОТНИТЕЛЯ
 */
@Getter
@Setter
public class OpLevelingSealer extends OpData {

    private String name = "";  //наименование

    private double compA = 0.0; //Компонент полиэфирный А
    private double compB = 0.0; //Компонент изоцинат Б

    private ESealersWidth sealersWidth = ESealersWidth.W10; //Ширина уплотнителя
    private Integer paramA = 0; //Размер А
    private Integer paramB = 0;//Размер Б

    public OpLevelingSealer() {
        super.normType = ENormType.NORM_ASSEMBLING;
        super.opType = EOpType.LEVELING_SEALER;
    }

    @Override
    public String toString() {
        return "наименование : " + name +
                ",\nкомпонент А = " + compA + " кг." +
                ", компонент Б = " + compB + " кг." +
                ", W проф = " + sealersWidth.getName() + " мм." +
                ", А = " + paramA + " мм." +
                ", Б = " + paramB + " мм.";
    }
}
