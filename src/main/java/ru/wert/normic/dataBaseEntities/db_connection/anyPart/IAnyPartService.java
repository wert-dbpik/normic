package ru.wert.normic.dataBaseEntities.db_connection.anyPart;


import ru.wert.normic.dataBaseEntities.db_connection.ItemService;

public interface IAnyPartService extends ItemService<AnyPart> {

    AnyPart findByName(String name);
}
