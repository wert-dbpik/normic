package ru.wert.normic.entities.db_connection.anyPartGroup;


import ru.wert.normic.entities.db_connection.ItemService;

public interface IAnyPartGroupService extends ItemService<AnyPartGroup> {

    AnyPartGroup findByName(String name);

}
