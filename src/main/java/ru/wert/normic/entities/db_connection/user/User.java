package ru.wert.normic.entities.db_connection.user;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import ru.wert.normic.entities.db_connection.Item;
import ru.wert.normic.entities.db_connection.UserGroup.UserGroup;
import ru.wert.normic.entities.db_connection._BaseEntity;

@Slf4j
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"name"}, callSuper = false)
public class User extends _BaseEntity implements Item {

    private String name;
    private String password;
    private UserGroup userGroup;
    private boolean logging; //следует ли пользователя логировать
    private boolean active; //активный ли пользователь

    @Override
    public String toUsefulString() {
        return name;
    }
}
