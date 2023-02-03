package ru.wert.normic.entities.db_connection.material;


import lombok.*;
import lombok.extern.slf4j.Slf4j;
import ru.wert.normic.entities.db_connection.Item;
import ru.wert.normic.entities.db_connection._BaseEntity;

import java.io.Serializable;

@Slf4j
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"name"}, callSuper = false)
public class MatType extends _BaseEntity implements Item, Serializable {

    private String name;
    private String note;

    @Override
    public String toUsefulString() {
        return name;
    }
}
