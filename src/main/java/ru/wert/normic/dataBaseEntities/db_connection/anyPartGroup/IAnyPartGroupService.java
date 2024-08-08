package ru.wert.normic.dataBaseEntities.db_connection.anyPartGroup;


import ru.wert.normic.dataBaseEntities.db_connection.ItemService;

public interface IAnyPartGroupService extends ItemService<AnyPartGroup> {

    AnyPartGroup findByName(String name);

}
