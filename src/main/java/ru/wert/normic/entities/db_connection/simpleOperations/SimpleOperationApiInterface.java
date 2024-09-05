package ru.wert.normic.entities.db_connection.simpleOperations;

import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface SimpleOperationApiInterface {

    @GET("simple-operations/id/{id}")
    Call<SimpleOperation> getById(@Path("id") Long id);

    @GET("simple-operations/all")
    Call<List<SimpleOperation>> getAll();

    @GET("simple-operations/name/{name}")
    Call<SimpleOperation> getByName(@Path("name") String name);

    @GET("simple-operations/all-by-text/{text}")
    Call<List<SimpleOperation>> getAllByText(@Path("text") String text);

    @POST("simple-operations/create")
    Call<SimpleOperation> create(@Body SimpleOperation entity);

    @PUT("simple-operations/update")
    Call<Void> update(@Body SimpleOperation entity);

    @DELETE("simple-operations/delete/{id}")
    Call<Void> deleteById(@Path("id") Long id);

}
