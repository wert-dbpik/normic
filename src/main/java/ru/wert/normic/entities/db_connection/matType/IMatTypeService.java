package ru.wert.normic.entities.db_connection.matType;


import ru.wert.normic.entities.db_connection.ItemService;

public interface IMatTypeService extends ItemService<MatType> {

    MatType findByName(String name);

}
