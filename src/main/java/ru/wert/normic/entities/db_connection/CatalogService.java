package ru.wert.normic.entities.db_connection;

import java.util.List;

public interface CatalogService<P extends Item>{

    List<P> findAllByGroupId(Long id);

    List<P> findAll();


}
