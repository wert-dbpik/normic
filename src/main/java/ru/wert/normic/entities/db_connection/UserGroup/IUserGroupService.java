package ru.wert.normic.entities.db_connection.UserGroup;


import ru.wert.normic.entities.db_connection.ItemService;

public interface IUserGroupService extends ItemService<UserGroup> {

    UserGroup findByName(String name);

}
