package ru.wert.normic.entities.ops.opAssembling;

import lombok.Getter;
import lombok.Setter;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.enums.EMaterialMeasurement;
import ru.wert.normic.enums.ENormType;
import ru.wert.normic.enums.EOpType;
import ru.wert.normic.enums.ESealersWidth;

import static java.lang.String.format;

/**
 * НАЛИВКА УПЛОТНИТЕЛЯ
 */
@Getter
@Setter
public class OpThermoInsulation extends OpData {

    private Integer thickness = 10;//Толщина термоизоляции

    private Integer height = 0;//Высота
    private Integer width = 0;//Ширина
    private Integer depth = 0;//Глубина

    private Double plusRate = 1.3; //Запас 30% по умолчанию
    private Boolean front = true; //уситывать переднюю плоскость (дверь)
    private Boolean back = true; //уситывать заднюю плоскость (заднюю стенку)
    private Double layout = 0.0; //Расход с запасом
    private EMaterialMeasurement measurement = EMaterialMeasurement.M2; //Единица измерения для материала

    public OpThermoInsulation() {
        super.normType = ENormType.NORM_ASSEMBLING;
        super.opType = EOpType.THERMO_INSULATION;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append(format("Термоизоляция толщиной %s мм", thickness)).append("\n")
                .append("Шкаф(")
                .append(format("В = %s; ",height))
                .append(format("Ш = %s; ",width))
                .append(format("Г = %s). ",depth)).append("\n")
                .append(format("Расход %s %s ", layout, measurement.getMeasure()))
                .append(format("с запасом %s%%", (plusRate * 100) - 100));

        return str.toString();

    }
}
