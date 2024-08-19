package ru.wert.normic.entities.db_connection;


import ru.wert.normic.entities.db_connection.anyPart.AnyPart;

public interface CatalogableItem extends Item{

    CatalogGroup getCatalogGroup();

    void setCatalogGroup(CatalogGroup catalogGroup);

    AnyPart getAnyPart();

}
