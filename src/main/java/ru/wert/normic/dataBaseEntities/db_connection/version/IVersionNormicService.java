package ru.wert.normic.dataBaseEntities.db_connection.version;

import ru.wert.normic.dataBaseEntities.db_connection.ItemService;

public interface IVersionNormicService extends ItemService<VersionNormic> {

    VersionNormic findByName(String name);

}
