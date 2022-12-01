package ru.wert.normik.entities.db_connection;


public interface CatalogableItem extends Item{

    CatalogGroup getCatalogGroup();

    void setCatalogGroup(CatalogGroup catalogGroup);

    AnyPart getAnyPart();

}
