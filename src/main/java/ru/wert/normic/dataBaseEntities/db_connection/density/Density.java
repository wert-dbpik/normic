package ru.wert.normic.dataBaseEntities.db_connection.density;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import ru.wert.normic.dataBaseEntities.db_connection.Item;
import ru.wert.normic.dataBaseEntities.db_connection._BaseEntity;

@Slf4j
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"name"}, callSuper = false)
public class Density extends _BaseEntity implements Item {

    private String name;
    private double amount;
    private String note;

    @Override
    public String toUsefulString() {
        return name;
    }
}
