package ru.wert.normic.entities.db_connection.simpleOperations;

import java.util.List;

public class SimpleOperationServiceImpl implements ISimpleOperationsService{

    static SimpleOperationServiceImpl instance;

    ISimpleOperationsService impl;

    public static SimpleOperationServiceImpl getInstance() {
        if (instance == null)
            return new SimpleOperationServiceImpl();
        return instance;
    }

    public SimpleOperationServiceImpl() {
        impl = new SimpleOperationTestService();
    }

    @Override
    public SimpleOperation findById(Long id) {
        return impl.findById(id);
    }

    @Override
    public SimpleOperation save(SimpleOperation simpleOperation) {
        return impl.save(simpleOperation);
    }

    @Override
    public boolean update(SimpleOperation simpleOperation) {
        return impl.update(simpleOperation);
    }

    @Override
    public boolean delete(SimpleOperation simpleOperation) {
        return impl.delete(simpleOperation);
    }

    @Override
    public List<SimpleOperation> findAll() {
        return impl.findAll();
    }

    @Override
    public List<SimpleOperation> findAllByText(String text) {
        return null; //не используется
    }
}
