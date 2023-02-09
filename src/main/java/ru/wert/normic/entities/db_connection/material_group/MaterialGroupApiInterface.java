package ru.wert.normic.entities.db_connection.material_group;

import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface MaterialGroupApiInterface {

    @GET("material-tree-groups/id/{id}")
    Call<MaterialGroup> getById(@Path("id") Long id);

    @GET("material-tree-groups/name/{name}")
    Call<MaterialGroup> getByName(@Path("name") String name);

    @GET("material-tree-groups/all")
    Call<List<MaterialGroup>> getAll();

    @GET("material-tree-groups/all-by-text/{text}")
    Call<List<MaterialGroup>> getAllByText(@Path("text") String text);

    @POST("material-tree-groups/create")
    Call<MaterialGroup> create(@Body MaterialGroup entity);

    @PUT("material-tree-groups/update")
    Call<Void> update(@Body MaterialGroup entity);

    @DELETE("material-tree-groups/delete/{id}")
    Call<Void> deleteById(@Path("id") Long id);

}
