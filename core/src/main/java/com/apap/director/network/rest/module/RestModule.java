package com.apap.director.network.rest.module;

import com.apap.director.network.rest.Paths;
import com.apap.director.network.rest.service.KeyService;
import com.apap.director.network.rest.service.UserService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

@Module
public class RestModule {

    private Retrofit retrofit;
    private final String baseUrl = "http://" + Paths.SERVER_IP + ":" + Paths.SERVER_PORT;

    public RestModule() {
        this.retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
    }

    @Provides
    @Singleton
    public UserService provideUserService() {
        return retrofit.create(UserService.class);
    }

    @Provides
    @Singleton
    public KeyService provideKeyService() {
        return retrofit.create(KeyService.class);
    }


}
