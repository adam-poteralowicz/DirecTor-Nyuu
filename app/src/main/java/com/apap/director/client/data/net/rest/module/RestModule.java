package com.apap.director.client.data.net.rest.module;

import com.apap.director.client.data.net.rest.Paths;
import com.apap.director.client.data.net.rest.service.KeyService;
import com.apap.director.client.data.net.rest.service.UserService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

@Module
public class RestModule {

    private static final String BASE_URL = "http://" + Paths.SERVER_IP + ":" + Paths.SERVER_PORT;
    private Retrofit retrofit;

    public RestModule() {
        this.retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
    }

    @Provides
    @Singleton
    UserService provideUserService() {
        return retrofit.create(UserService.class);
    }

    @Provides
    @Singleton
    KeyService provideKeyService() {
        return retrofit.create(KeyService.class);
    }


}
