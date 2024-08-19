package ru.wert.normic.entities.db_connection.density;


import ru.wert.normic.entities.db_connection.ItemService;

public interface IDensityService extends ItemService<Density> {

    Density findByName(String name);

    Density findByValue(double value);

}
