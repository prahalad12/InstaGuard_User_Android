package com.instaguard.user.Helpers.Retrofit;


import com.instaguard.user.BuildConfig;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.instaguard.user.Helpers.Utils;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {

    public static Retrofit getClient() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        //OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor)
                              .connectTimeout(1000, TimeUnit.SECONDS)
                               .readTimeout(1000, TimeUnit.SECONDS).build();

        Gson gson = new GsonBuilder().setLenient().registerTypeAdapterFactory(new NullStringToEmptyAdapterFactory()).create();

        Retrofit retrofit;
        if (BuildConfig.DEBUG) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(Utils.RETROFIT_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(client)
                    .build();
        }else {
            retrofit = new Retrofit.Builder()
                    .baseUrl(Utils.RETROFIT_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }

    public static Retrofit getClient_linked(String LINKED_IN_URL) {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        //OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor)
                              .connectTimeout(1000, TimeUnit.SECONDS)
                               .readTimeout(1000, TimeUnit.SECONDS).build();

        Gson gson = new GsonBuilder().setLenient().registerTypeAdapterFactory(new NullStringToEmptyAdapterFactory()).create();

        Retrofit retrofit;
        if (BuildConfig.DEBUG) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(LINKED_IN_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(client)
                    .build();
        }else {
            retrofit = new Retrofit.Builder()
                    .baseUrl(LINKED_IN_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }

    public static Retrofit getClient_chat() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        //OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor)
                .connectTimeout(1000, TimeUnit.SECONDS)
                .readTimeout(1000, TimeUnit.SECONDS).build();

        OkHttpClient client2 = new OkHttpClient.Builder().connectTimeout(1000, TimeUnit.SECONDS).
                readTimeout(1000, TimeUnit.SECONDS).build();

        Gson gson = new GsonBuilder().setLenient().registerTypeAdapterFactory(new NullStringToEmptyAdapterFactory()).create();

        Retrofit retrofit;
        if (BuildConfig.DEBUG) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(Utils.RETROFIT_URL_CALL_CHAT_ACCESS)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(client)
                    .build();
        } else {
            retrofit = new Retrofit.Builder()
                    .baseUrl(Utils.RETROFIT_URL_CALL_CHAT_ACCESS)
                    .client(client2)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }

        return retrofit;
    }

    static class NullStringToEmptyAdapterFactory<T> implements TypeAdapterFactory {
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {

            Class<T> rawType = (Class<T>) type.getRawType();
            if (rawType != String.class) {
                return null;
            }
            return (TypeAdapter<T>) new StringAdapter();
        }
    }
}
