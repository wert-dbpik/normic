package ru.wert.normic.entities.db_connection.user;


import ru.wert.normic.entities.db_connection.ItemService;

public interface IUserService extends ItemService<User> {

    User findByName(String name);
    User findByPassword(String pass);

}
