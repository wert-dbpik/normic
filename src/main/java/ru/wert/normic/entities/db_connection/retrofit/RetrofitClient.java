package ru.wert.normic.entities.db_connection.retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.concurrent.TimeUnit;


@Slf4j
public class RetrofitClient{
    private static final String TAG = "RetrofitClient";

    public static String baseUrl = "http://192.168.2.132:8080";

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


        String ip = AppProperties.getInstance().getIpAddress();
        String port = AppProperties.getInstance().getPort();
        baseUrl = "http://"+ip +":"+ port;
        mRetrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(new NullOnEmptyConverterFactory()) //Исправляет исключение на null, когда приходит пустое тело
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient.build()) //борьба с readTimeout
//                .client(client.build()) // логгирование ответа
//                .client(client1.build()) // json forever!
                .build();

    }

    public static void restartWithNewUrl() {
        new RetrofitClient();
    }

    public Retrofit getRetrofit() {
        return mRetrofit;
    }

}



