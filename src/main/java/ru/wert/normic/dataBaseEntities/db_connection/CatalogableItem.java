package ru.wert.normic.dataBaseEntities.db_connection;


import ru.wert.normic.dataBaseEntities.db_connection.anyPart.AnyPart;

public interface CatalogableItem extends Item{

    CatalogGroup getCatalogGroup();

    void setCatalogGroup(CatalogGroup catalogGroup);

    AnyPart getAnyPart();

}
