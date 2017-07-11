package com.apap.director.net;

import android.content.Context;

import com.apap.director.network.rest.Paths;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Adam Poterałowicz
 */

@Module
public class NetModule {

    //only in affected classes' component

    private static final String BASE_URL = "http://" + Paths.SERVER_IP + ":" + Paths.SERVER_PORT;
    private OkHttpClient client;

    public NetModule(OkHttpClient okHttpClient) {
        this.client = okHttpClient;
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient() {
        return client;
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build();
    }

}
