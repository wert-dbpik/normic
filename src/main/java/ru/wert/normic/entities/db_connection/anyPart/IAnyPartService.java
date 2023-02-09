package ru.wert.normic.entities.db_connection.anyPart;


import ru.wert.normic.entities.db_connection.ItemService;

public interface IAnyPartService extends ItemService<AnyPart> {

    AnyPart findByName(String name);
}
