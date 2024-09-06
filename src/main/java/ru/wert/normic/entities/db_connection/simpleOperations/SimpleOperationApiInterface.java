package ru.wert.normic.entities.db_connection.simpleOperations;

import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface SimpleOperationApiInterface {

    @GET("simple_operations/id/{id}")
    Call<SimpleOperation> getById(@Path("id") Long id);

    @GET("simple_operations/all")
    Call<List<SimpleOperation>> getAll();

    @GET("simple_operations/name/{name}")
    Call<SimpleOperation> getByName(@Path("name") String name);

    @GET("simple_operations/all-by-text/{text}")
    Call<List<SimpleOperation>> getAllByText(@Path("text") String text);

    @POST("simple_operations/create")
    Call<SimpleOperation> create(@Body SimpleOperation entity);

    @PUT("simple_operations/update")
    Call<Void> update(@Body SimpleOperation entity);

    @DELETE("simple_operations/delete/{id}")
    Call<Void> deleteById(@Path("id") Long id);

}
