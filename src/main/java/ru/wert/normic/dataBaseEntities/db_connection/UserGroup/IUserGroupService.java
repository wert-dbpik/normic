package ru.wert.normic.dataBaseEntities.db_connection.UserGroup;


import ru.wert.normic.dataBaseEntities.db_connection.ItemService;

public interface IUserGroupService extends ItemService<UserGroup> {

    UserGroup findByName(String name);

}
