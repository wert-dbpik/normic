package ru.wert.normic.dataBaseEntities.db_connection.UserGroup;

import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface UserGroupApiInterface {

    @GET("user-groups/id/{id}")
    Call<UserGroup> getById(@Path("id") Long id);

    @GET("user-groups/name/{name}")
    Call<UserGroup> getByName(@Path("name") String name);

    @GET("user-groups/all")
    Call<List<UserGroup>> getAll();

    @GET("user-groups/all-by-text/{text}")
    Call<List<UserGroup>> getAllByText(@Path("text") String tetx);

    @POST("user-groups/create")
    Call<UserGroup> create(@Body UserGroup entity);

    @PUT("user-groups/update")
    Call<Void> update(@Body UserGroup entity);

    @DELETE("user-groups/delete/{id}")
    Call<Void> deleteById(@Path("id") Long id);

}
