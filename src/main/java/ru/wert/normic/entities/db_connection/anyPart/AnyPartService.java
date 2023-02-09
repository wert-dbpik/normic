package ru.wert.normic.entities.db_connection.anyPart;

import retrofit2.Call;
import ru.wert.normic.entities.db_connection.ItemService;
import ru.wert.normic.entities.db_connection.anyPartGroup.AnyPartGroupService;
import ru.wert.normic.entities.db_connection.retrofit.RetrofitClient;

import java.io.IOException;
import java.util.List;

;

public class AnyPartService implements IAnyPartService, ItemService<AnyPart> {

    private static AnyPartService instance;
    private AnyPartApiInterface api;

    private AnyPartService() {
        api = RetrofitClient.getInstance().getRetrofit().create(AnyPartApiInterface.class);
    }

    public AnyPartApiInterface getApi() {
        return api;
    }

    public static AnyPartService getInstance() {
        if (instance == null)
            return new AnyPartService();
        return instance;
    }

    @Override
    public AnyPart findById(Long id) {
        try {
            Call<AnyPart> call = api.getById(id);
            return call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public AnyPart findByName(String name) {
        try {
            Call<AnyPart> call = api.getByName(name);
            return call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<AnyPart> findAll() {
        try {
            Call<List<AnyPart>> call = api.getAll();
            return call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<AnyPart> findAllByText(String text) {
        try {
            Call<List<AnyPart>> call = api.getAllByText(text);
            return call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public AnyPart save(AnyPart entity) {

        try {
            Call<AnyPart> call = api.create(entity);
            return call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean update(AnyPart entity) {
        try {
            Call<Void> call = api.update(entity);
            return call.execute().isSuccessful();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(AnyPart entity) {
        Long id = entity.getId();
        try {
            Call<Void> call = api.deleteById(id);
            return call.execute().isSuccessful();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * Создаем и сохраняем в БД новый элемент, если он еще не существует
     * @param partGroup
     * @param name
     * @return
     */
    public AnyPart createNewPart(Long partGroup, String name, String secondName){
        AnyPart part = findByName(name);
        if(part == null){
            part = new AnyPart(name, secondName, AnyPartGroupService.getInstance().findById(partGroup));
            save(part);
        }
        return part;
    }


}
