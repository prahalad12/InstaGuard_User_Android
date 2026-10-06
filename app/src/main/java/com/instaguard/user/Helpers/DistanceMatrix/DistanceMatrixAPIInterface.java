package com.instaguard.user.Helpers.DistanceMatrix;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;


public class DistanceMatrixAPIInterface {
    private static DistanceMatrixAPIInterface self;
    private String API_BASE_URL = "http://maps.googleapis.com/";
    private Retrofit retrofit;

    private DistanceMatrixAPIInterface() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient httpClient = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        Retrofit.Builder builder =
                new Retrofit.Builder()
                        .baseUrl(API_BASE_URL)
                        .addConverterFactory(JacksonConverterFactory.create());
        retrofit = builder.client(httpClient).build();
    }

    public static DistanceMatrixAPIInterface getInstance() {
        if (self == null) {
            synchronized(DistanceMatrixAPIInterface.class) {
                if (self == null) {
                    self = new DistanceMatrixAPIInterface();
                }
            }
        }
        return self;
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }

}
