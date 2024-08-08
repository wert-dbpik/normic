package ru.wert.normic.dataBaseEntities.db_connection.othersOps;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import ru.wert.normic.dataBaseEntities.db_connection.Item;
import ru.wert.normic.dataBaseEntities.db_connection._BaseEntity;
import ru.wert.normic.enums.*;

@Slf4j
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"name"}, callSuper = false)
public class SimpleOperation extends _BaseEntity implements Item {

    private String name; //Наименование операции
    private EPieceMeasurement measurement; //Единицы измерения количество изготовленного
    private double norm; //Норма времени (мин) на единицу изготовленного
    private ENormType normType; //Цех (тип нормы )
    private EJobType jobType = EJobType.JOB_NONE; //Тип работы для ENormType.MECHANICAL
    private String description; //Описание операции


    @Override
    public String toUsefulString() {
        return name;
    }
}
