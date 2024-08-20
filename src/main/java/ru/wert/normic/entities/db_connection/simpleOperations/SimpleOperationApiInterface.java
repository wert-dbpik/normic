package ru.wert.normic.entities.db_connection.simpleOperations;

import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface SimpleOperationApiInterface {

    @GET("simpleops/id/{id}")
    Call<SimpleOperation> getById(@Path("id") Long id);

    @GET("simpleops/all")
    Call<List<SimpleOperation>> getAll();

    @POST("simpleops/create")
    Call<SimpleOperation> create(@Body SimpleOperation entity);

    @PUT("simpleops/update")
    Call<Void> update(@Body SimpleOperation entity);

    @DELETE("simpleops/delete/{id}")
    Call<Void> deleteById(@Path("id") Long id);

}
