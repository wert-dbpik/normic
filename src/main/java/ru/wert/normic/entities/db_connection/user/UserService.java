package ru.wert.normic.entities.db_connection.user;

import lombok.extern.slf4j.Slf4j;
import retrofit2.Call;
import ru.wert.normic.entities.db_connection.ItemService;
import ru.wert.normic.entities.db_connection.retrofit.RetrofitClient;

import java.io.IOException;
import java.util.List;

@Slf4j
public class UserService implements IUserService, ItemService<User> {

    private static UserService instance;
    private UserApiInterface api;

    private UserService() {
        api = RetrofitClient.getInstance().getRetrofit().create(UserApiInterface.class);
    }

    public UserApiInterface getApi() {
        return api;
    }

    public static UserService getInstance() {
        if (instance == null)
            return new UserService();
        return instance;
    }

    @Override
    public User findById(Long id) {
        try {
            Call<User> call = api.getById(id);
            return call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public User findByName(String name) {
        try {
            Call<User> call = api.getByName(name);
            return call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public User findByPassword(String pass) {
        try {
            Call<User> call = api.getByPassword(pass);
            return call.execute().body();
        } catch (IOException e) {
            log.info("Неудачная попытка войти по паролю {}", pass);
        }
        return null;
    }

    @Override
    public List<User> findAll() {
        try {
            Call<List<User>> call = api.getAll();
            return call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<User> findAllByText(String text) {
        try {
            Call<List<User>> call = api.getAllByText(text);
            return call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public User save(User entity) {
        try {
            Call<User> call = api.create(entity);
            return call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean update(User entity) {
        try {
            Call<Void> call = api.update(entity);
            return call.execute().isSuccessful();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(User entity) {
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
