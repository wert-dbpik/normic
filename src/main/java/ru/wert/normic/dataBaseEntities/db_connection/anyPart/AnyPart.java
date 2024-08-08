package ru.wert.normic.dataBaseEntities.db_connection.anyPart;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import ru.wert.normic.dataBaseEntities.db_connection.Item;
import ru.wert.normic.dataBaseEntities.db_connection._BaseEntity;
import ru.wert.normic.dataBaseEntities.db_connection.anyPartGroup.AnyPartGroup;

import java.io.Serializable;

@Slf4j
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"name", "secondName"}, callSuper = false)
public class AnyPart extends _BaseEntity implements Item, Serializable {

    private String name;
    private String secondName;
    private AnyPartGroup anyPartGroup;

    @Override
    public String toUsefulString() {
        if(secondName == null)
            return name;
        return name + " : " + secondName;
    }

    @Override
    public String toString() {
        return "AnyPart{" +
                ", id=" + id +
                "name='" + name + '\'' +
                ", secondName='" + secondName + '\'' +
                ", anyPartGroup=" + anyPartGroup +
                '}';
    }
}
