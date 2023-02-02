package ru.wert.normic.entities.db_connection;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

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
