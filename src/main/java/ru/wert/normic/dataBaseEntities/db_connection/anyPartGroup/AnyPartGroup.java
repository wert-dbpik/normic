package ru.wert.normic.dataBaseEntities.db_connection.anyPartGroup;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import ru.wert.normic.dataBaseEntities.db_connection.Item;
import ru.wert.normic.dataBaseEntities.db_connection._BaseEntity;

import java.io.Serializable;

@Slf4j
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"name"}, callSuper = false)
public class AnyPartGroup extends _BaseEntity implements Item, Serializable {

    private Long parentId;
    private String name;
//    private List<AnyPart> partsInGroup;


    @Override
    public String toUsefulString() {
        return name;
    }
}
