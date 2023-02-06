package ru.wert.normic.materials;

import ru.wert.normic.entities.db_connection.material.Material;

public interface MatTypeController {

    MatTypeController getController();

    void fillData(Material material);
}
