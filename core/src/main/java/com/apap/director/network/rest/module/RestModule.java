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

    private final String baseUrl = "http://" + Paths.SERVER_IP + ":" + Paths.SERVER_PORT;

    public RestModule() {
    }

    @Provides
    @Singleton
    public UserService provideUserService(Retrofit retrofit) {
        return retrofit.create(UserService.class);
    }

    @Provides
    @Singleton
    public KeyService provideKeyService(Retrofit retrofit) {
        return retrofit.create(KeyService.class);
    }


}
