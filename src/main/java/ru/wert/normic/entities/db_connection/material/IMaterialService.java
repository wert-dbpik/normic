package ru.wert.normic.entities.db_connection.material;

import ru.wert.normic.entities.db_connection.ItemService;

public interface IMaterialService extends ItemService<Material> {

    Material findByName(String name);

    Material createFakeProduct(String name);
}
