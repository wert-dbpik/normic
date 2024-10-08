package ru.wert.normic.entities.db_connection.retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import ru.wert.normic.controllers.intro.ConnectionParams;
import ru.wert.normic.entities.db_connection.user.User;

import java.io.IOException;
import java.util.concurrent.TimeUnit;


@Slf4j
public class RetrofitClient{
    private static final String TAG = "RetrofitClient";

    public static ConnectionParams params;

    private static RetrofitClient instance;
    private static Retrofit mRetrofit;
    private Gson gson;

    public static RetrofitClient getInstance() {
        if (instance == null) {
            instance = new RetrofitClient();
        }
        return instance;
    }

    /**
     * Приватный конструктор
     */
    private RetrofitClient() {

        gson = new GsonBuilder()
                .setLenient()
                .create();

        //борьба с readTimeout
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .callTimeout(2, TimeUnit.MINUTES)
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS);

        //Перехватчик для логгирования запросов и ответов
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder client = new OkHttpClient.Builder()
                .addInterceptor(interceptor);

        String baseUrl = "http://"+params.getIp() +":"+ params.getPort();
        mRetrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(new NullOnEmptyConverterFactory()) //Исправляет исключение на null, когда приходит пустое тело
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient.build()) //борьба с readTimeout
//                .client(client.build()) // логгирование ответа
//                .client(client1.build()) // json forever!
                .build();

    }

    public static void restartWithNewUrl(ConnectionParams newParams) {
        params = newParams;
        new RetrofitClient();
    }

    public Retrofit getRetrofit() {
        return mRetrofit;
    }

    /**
     * Проверка работы ретрофита
     * Если найдется пользователь с id = 1, то ретрофит и соединение настроены
     */
    public static boolean checkUpConnection(){
        log.debug("checkUpConnection : is starting ...");
        CheckUpConnectionInterface api = RetrofitClient.getInstance().getRetrofit().create(CheckUpConnectionInterface.class);
        try {
            Call<User> call = api.getById(1L);
            call.execute().body();
        } catch (IOException e) {
            return false;
        }
        log.info("checkUpConnection : connection to Data Base is OK");
        return true;
    }

    /**
     * Интерфейс для проверки соединения
     */
    interface CheckUpConnectionInterface{
        @GET("users/id/{id}")
        Call<User> getById(@Path("id") Long id);
    }

}



