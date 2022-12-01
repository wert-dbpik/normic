package ru.wert.normik.entities.db_connection;

public interface IMaterialService extends ItemService<Material> {

    Material findByName(String name);

    Material createFakeProduct(String name);
}
