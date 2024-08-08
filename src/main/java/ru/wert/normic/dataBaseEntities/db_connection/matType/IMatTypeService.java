package ru.wert.normic.dataBaseEntities.db_connection.matType;


import ru.wert.normic.dataBaseEntities.db_connection.ItemService;

public interface IMatTypeService extends ItemService<MatType> {

    MatType findByName(String name);

}
