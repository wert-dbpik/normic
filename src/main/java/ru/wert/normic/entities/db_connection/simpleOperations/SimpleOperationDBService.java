package ru.wert.normic.entities.db_connection.simpleOperations;


import retrofit2.Call;
import ru.wert.normic.entities.db_connection.ItemService;
import ru.wert.normic.entities.db_connection.retrofit.RetrofitClient;

import java.io.IOException;
import java.util.List;

public class SimpleOperationDBService implements ISimpleOperationsService{

    private static SimpleOperationDBService instance;
    private SimpleOperationApiInterface api;

    public SimpleOperationDBService() {
        api = RetrofitClient.getInstance().getRetrofit().create(SimpleOperationApiInterface.class);
    }

    public SimpleOperationApiInterface getApi() {
        return api;
    }

//    public static SimpleOperationDBService getInstance() {
//        if (instance == null)
//            return new SimpleOperationDBService();
//        return instance;
//    }

    @Override
    public SimpleOperation findById(Long id) {
        try {
            Call<SimpleOperation> call = api.getById(id);
            return call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<SimpleOperation> findAll() {
        try {
            Call<List<SimpleOperation>> call = api.getAll();
            return call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<SimpleOperation> findAllByText(String text) {
        return null; //НЕ ИСПОЛЬЗУЕТСЯ
    }


    @Override
    public SimpleOperation save(SimpleOperation entity) {
        try {
            Call<SimpleOperation> call = api.create(entity);
            return call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean update(SimpleOperation entity) {
        try {
            Call<Void> call = api.update(entity);
            return call.execute().isSuccessful();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(SimpleOperation entity) {
        Long id = entity.getId();
        try {
            Call<Void> call = api.deleteById(id);
            return call.execute().isSuccessful();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }


}