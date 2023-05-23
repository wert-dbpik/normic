package ru.wert.normic.entities.db_connection.version;

import ru.wert.normic.entities.db_connection.ItemService;

public interface IVersionNormicService extends ItemService<VersionNormic> {

    VersionNormic findByName(String name);

}
