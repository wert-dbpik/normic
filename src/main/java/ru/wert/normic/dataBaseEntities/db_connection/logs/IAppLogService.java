package ru.wert.normic.dataBaseEntities.db_connection.logs;

import ru.wert.normic.dataBaseEntities.db_connection.ItemService;
import ru.wert.normic.dataBaseEntities.db_connection.user.User;

import java.time.LocalDateTime;
import java.util.List;


public interface IAppLogService extends ItemService<AppLog> {

    AppLog findByName(String name);
    List<AppLog> findAllByTimeBetween(LocalDateTime startTime, LocalDateTime finishTime);
    List<AppLog> findAllByTimeBetweenAndAdminOnlyFalse(LocalDateTime startTime, LocalDateTime finishTime);
    List<AppLog> findAllByUser(User user);
    List<AppLog> findAllByUserAndAdminOnlyFalse(User user);
    List<AppLog> findAllByApplication(Integer app);
    List<AppLog> findAllByApplicationAndAdminOnlyFalse(Integer app);
    List<AppLog> findAllByAdminOnlyFalse();

}
