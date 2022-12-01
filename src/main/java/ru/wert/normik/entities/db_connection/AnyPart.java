package ru.wert.normik.entities.db_connection;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"name", "secondName"}, callSuper = false)
public class AnyPart extends _BaseEntity implements Item {

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
