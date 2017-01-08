package com.apap.director.network.rest.module;

import com.apap.director.network.rest.Paths;
import com.apap.director.network.rest.service.KeyService;
import com.apap.director.network.rest.service.UserService;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.UnknownHostException;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

@Module
public class RestModule {

    private Retrofit retrofit;
    private final String baseUrl = "http://"+Paths.SERVER_IP+":"+Paths.SERVER_PORT;

    public RestModule() {
        this.retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(JacksonConverterFactory.create())
                .client(getMyHttpClient())
                .build();
    }

    private OkHttpClient getMyHttpClient() {
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .proxy(new Proxy(Proxy.Type.SOCKS, new InetSocketAddress("78.88.208.143", 16596)))
                .build();

        return httpClient;
    }

    @Provides
    @Singleton
    public UserService provideUserService(){
        return retrofit.create(UserService.class);
    }

    @Provides
    @Singleton
    public KeyService provideKeyService(){
        return retrofit.create(KeyService.class);
    }


}
