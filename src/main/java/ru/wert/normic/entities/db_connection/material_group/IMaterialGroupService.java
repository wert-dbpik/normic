package ru.wert.normic.entities.db_connection.material_group;


import ru.wert.normic.entities.db_connection.ItemService;

public interface IMaterialGroupService extends ItemService<MaterialGroup> {

    MaterialGroup findByName(String name);

    MaterialGroup getRootItem();
}
