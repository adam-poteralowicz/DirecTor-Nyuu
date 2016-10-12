package com.apap.director.im.domain.message.module;

import com.apap.director.im.domain.message.event.MessageEventListener;

import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

@Module
public class MessageModule {

    @Provides
    @Singleton
    public MessageEventListener provideMessageEventListener(){
        return new MessageEventListener();
    }

}
