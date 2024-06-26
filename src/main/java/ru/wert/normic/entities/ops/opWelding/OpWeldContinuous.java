package ru.wert.normic.entities.ops.opWelding;

import lombok.Getter;
import lombok.Setter;
import ru.wert.normic.controllers.listOperations.counters.OpCuttingCounter;
import ru.wert.normic.controllers.welding.counters.OpWeldContinuousCounter;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.enums.EJobType;
import ru.wert.normic.enums.ENormType;
import ru.wert.normic.enums.EOpType;
import ru.wert.normic.enums.EPartBigness;
import ru.wert.normic.interfaces.NormCounter;

/**
 * СВАРКА НЕПРЕРЫВНЫМ ШВОМ
 */
@Getter
@Setter
public class OpWeldContinuous extends OpData {

    private String name = "";
    private Integer seamLength = 0; //Длина шва
    private EPartBigness partBigness =EPartBigness.SMALL; //размер собираемых деталей
    private Integer men = 1; //Число человек, работающих над операцией
    private boolean stripping = false; //Использовать зачистку
    private boolean preEnterSeams = true; //количество швов вводить вручную
    private Integer seams = 1; //Количество швов заданное пользователем
    private Integer connectionLength = 0; //Длина сединения на которую расчитывается количество точек
    private Integer step = 0; //шаг точек

    public OpWeldContinuous() {
        super.normType = ENormType.NORM_MECHANICAL;
        super.jobType = EJobType.JOB_WELDING;
        super.opType = EOpType.WELD_CONTINUOUS;
    }

    @Override
    public String toString() {
        return "наименование : " + name +
                ",\nL шва = " + seamLength + " мм." +
                ", размер сборки = " + partBigness.getName() +
                ", N рабочих = " + men +
                ",\nзачистка швов = " + stripping +
                ", N швов = " + seams +
                ", L соед = " + connectionLength + " мм." +
                ", шаг = " + step + " мм.";
    }
}
