package ru.wert.normic.entities.db_connection.simpleOperations;

import ru.wert.normic.entities.db_connection.ItemService;
import ru.wert.normic.entities.db_connection.material.Material;

import java.util.List;

public interface ISimpleOperationsService extends ItemService<SimpleOperation> {

    SimpleOperation findByName(String name);

    List<SimpleOperation> findAllByText(String text);
}
