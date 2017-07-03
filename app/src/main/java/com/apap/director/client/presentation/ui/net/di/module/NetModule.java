package com.apap.director.client.presentation.ui.net.di.module;

import com.apap.director.client.data.net.rest.Paths;
import com.apap.director.client.data.net.rest.service.KeyService;
import com.apap.director.client.data.net.rest.service.UserService;
import com.apap.director.client.data.net.service.HttpService;
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
    private final OkHttpClient client;

    public NetModule(OkHttpClient client) {
        this.client = client;
    }

    @Provides
    @Singleton
    UserService provideUserService(Retrofit retrofit) {
        return retrofit.create(UserService.class);
    }

    @Provides
    @Singleton
    KeyService provideKeyService(Retrofit retrofit) {
        return retrofit.create(KeyService.class);
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient() {
        return client;
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build();
    }

    @Provides
    @Singleton
    WebSocketService provideWebSocketService() {
        return new WebSocketService();
    }

    @Provides
    @Singleton
    HttpService provideHttpService() {
        return new HttpService();
    }
}
