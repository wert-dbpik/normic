package ru.wert.normic.dataBaseEntities.db_connection.material;


import retrofit2.Call;
import ru.wert.normic.dataBaseEntities.db_connection.Item;
import ru.wert.normic.dataBaseEntities.db_connection.ItemService;
import ru.wert.normic.dataBaseEntities.db_connection.PartItem;
import ru.wert.normic.dataBaseEntities.db_connection.retrofit.RetrofitClient;


import java.io.IOException;
import java.util.List;

public class MaterialService implements IMaterialService, ItemService<Material>, PartItem {

    private static MaterialService instance;
    private MaterialApiInterface api;

    private MaterialService() {
        api = RetrofitClient.getInstance().getRetrofit().create(MaterialApiInterface.class);
    }

    public MaterialApiInterface getApi() {
        return api;
    }

    public static MaterialService getInstance() {
        if (instance == null)
            return new MaterialService();
        return instance;
    }

    @Override
    public Material findById(Long id) {
        try {
            Call<Material> call = api.getById(id);
            return call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Material findByName(String name) {
        try {
            Call<Material> call = api.getByName(name);
            return call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Material> findAll() {
        try {
            Call<List<Material>> call = api.getAll();
            return call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Material> findAllByText(String text) {
        try {
            Call<List<Material>> call = api.getAllByText(text);
            return call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Material save(Material entity) {
        try {
            Call<Material> call = api.create(entity);
            return call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean update(Material entity) {
        try {
            Call<Void> call = api.update(entity);
            return call.execute().isSuccessful();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(Material entity) {
        Long id = entity.getId();
        try {
            Call<Void> call = api.deleteById(id);
            return call.execute().isSuccessful();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    //Получаем строку из модели Класса, там ее удобнее написать

    @Override
    public Material createFakeProduct(String name){
        return new Material(name);
    }

    @Override
    public String getPartName(Item material) {
        return material.getName();
    }


}