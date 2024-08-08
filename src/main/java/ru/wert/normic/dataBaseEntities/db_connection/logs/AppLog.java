package ru.wert.normic.dataBaseEntities.db_connection.logs;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import ru.wert.normic.dataBaseEntities.db_connection.Item;
import ru.wert.normic.dataBaseEntities.db_connection._BaseEntity;
import ru.wert.normic.dataBaseEntities.db_connection.user.User;

@Slf4j
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"user", "text"}, callSuper = false)
public class AppLog extends _BaseEntity implements Item {

    private String time;
    private boolean adminOnly;
    private User user;
    private Integer application;
    private String version;
    private String text;

    @Override
    public String getName() {
        return "";
    }

    @Override
    public String toUsefulString() {
        return user.getName() + ": " + text;
    }
}
