package ru.wert.normic.entities.welding;

import lombok.Getter;
import lombok.Setter;
import ru.wert.normic.entities.OpData;
import ru.wert.normic.enums.ENormType;
import ru.wert.normic.enums.EOpType;
import ru.wert.normic.enums.EPartBigness;

@Getter
@Setter
public class OpWeldContinuous extends OpData {

    private Integer seamLength = 0; //Длина шва
    private EPartBigness partBigness =EPartBigness.SMALL; //размер собираемых деталей
    private Integer men = 1; //Число человек, работающих над операцией
    private boolean stripping = false; //Использовать зачистку
    private boolean preEnterSeams = true; //количество швов вводить вручную
    private Integer seams = 0; //Количество швов заданное пользователем
    private Integer connectionLength = 0; //Длина сединения на которую расчитывается количество точек
    private Integer step = 0; //шаг точек

    public OpWeldContinuous() {
        super.normType = ENormType.NORM_MECHANICAL;
        super.opType = EOpType.WELD_CONTINUOUS;
    }
}
