package com.apap.director.im.domain.websocket.module;

import com.apap.director.im.domain.websocket.service.StompService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class WebSocketModule {

    public WebSocketModule(){
    }

    @Provides
    @Singleton
    public StompService provideStompService(){
        return new StompService();
    }
}
