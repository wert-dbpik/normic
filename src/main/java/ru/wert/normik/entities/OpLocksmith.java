package ru.wert.normik.entities;

import lombok.Getter;
import lombok.Setter;
import ru.wert.normik.enums.ENormType;
import ru.wert.normik.enums.EOpType;

@Getter
@Setter
public class OpLocksmith extends OpData {

    private Integer rivets = 0; //Вытяжные заклепки
    private Integer countersinkings = 0; //Зенкования
    private Integer threadings = 0; //Нарезания резьбы
    private Integer smallSawings = 0; //Пиление на малой пиле
    private Integer bigSawings = 0;//Пиление на большой пиле

    public OpLocksmith() {
        super.normType = ENormType.NORM_MECHANICAL;
        super.opType = EOpType.LOCKSMITH;
    }
}
