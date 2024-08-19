package ru.wert.normic.entities.db_connection.UserGroup;

import retrofit2.Call;
import ru.wert.normic.entities.db_connection.ItemService;
import ru.wert.normic.entities.db_connection.retrofit.RetrofitClient;

import java.io.IOException;
import java.util.List;

public class UserGroupService implements IUserGroupService, ItemService<UserGroup> {

    private static UserGroupService instance;
    private UserGroupApiInterface api;

    private UserGroupService() {
        api = RetrofitClient.getInstance().getRetrofit().create(UserGroupApiInterface.class);
    }

    public UserGroupApiInterface getApi() {
        return api;
    }

    public static UserGroupService getInstance() {
        if (instance == null)
            return new UserGroupService();
        return instance;
    }

    @Override
    public UserGroup findById(Long id) {
        try {
            Call<UserGroup> call = api.getById(id);
            return call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public UserGroup findByName(String name) {
        try {
            Call<UserGroup> call = api.getByName(name);
            return call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<UserGroup> findAll() {
        try {
            Call<List<UserGroup>> call = api.getAll();
            return call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<UserGroup> findAllByText(String text) {
        try {
            Call<List<UserGroup>> call = api.getAllByText(text);
            return call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public UserGroup save(UserGroup entity) {
        try {
            Call<UserGroup> call = api.create(entity);
            return call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean update(UserGroup entity) {
        try {
            Call<Void> call = api.update(entity);
            return call.execute().isSuccessful();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(UserGroup entity) {
        Long id = entity.getId();
        try {
            Call<Void> call = api.deleteById(id);
            return call.execute().isSuccessful();
//            int s = call.execute().code();
//            if(s == 500) throw new ItemIsBusyException();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

}
