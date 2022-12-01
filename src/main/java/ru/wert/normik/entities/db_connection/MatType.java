package ru.wert.normik.entities.db_connection;


import lombok.*;
import lombok.extern.slf4j.Slf4j;

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
