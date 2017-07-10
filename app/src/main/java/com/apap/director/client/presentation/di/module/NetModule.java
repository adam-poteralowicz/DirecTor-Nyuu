package com.apap.director.client.presentation.di.module;

import android.content.Context;

import com.apap.director.client.data.net.rest.Paths;
import com.apap.director.client.data.net.rest.service.KeyService;
import com.apap.director.client.data.net.rest.service.RestAccountService;
import com.apap.director.client.data.net.service.WebSocketService;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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
    RestAccountService provideRestAccountService(Retrofit retrofit) {
        return retrofit.create(RestAccountService.class);
    }

    @Provides
    @Singleton
    KeyService provideKeyService(Retrofit retrofit) {
        return retrofit.create(KeyService.class);
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient(Context context) {
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

    @Provides
    @Singleton
    WebSocketService provideWebSocketService() {
        return new WebSocketService();
    }

}
