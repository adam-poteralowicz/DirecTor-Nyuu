package com.apap.director.im.websocket.module;

import android.content.Context;

import com.apap.director.im.signal.DirectorIdentityKeyStore;
import com.apap.director.im.signal.DirectorPreKeyStore;
import com.apap.director.im.signal.DirectorSessionStore;
import com.apap.director.im.signal.DirectorSignedPreKeyStore;
import com.apap.director.im.websocket.service.MessageAction;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class WebSocketModule {

    private Context context;

    public WebSocketModule(Context context){

    }

//    @Provides
//    @Singleton
//    public StompService provideStompService(MessageAction messageAction){
//        return new StompService(messageAction);
//    }

//    @Provides
//    @Singleton
//    public StompService provideStompService(StompServiceConnection connection){
//
//        Intent bindingIntent = new Intent(context, StompService.class);
//        context.bindService(bindingIntent, connection, Context.BIND_AUTO_CREATE);
//
//
//        return connection.getService();
//    }

    @Provides
    @Singleton
    public MessageAction provideMessageAction(DirectorIdentityKeyStore identityKeyStore, DirectorSessionStore sessionStore, DirectorSignedPreKeyStore signedPreKeyStore, DirectorPreKeyStore preKeyStore){
        return new MessageAction(preKeyStore, identityKeyStore, sessionStore, signedPreKeyStore);
    }

//    @Provides
//    @Singleton
//    public StompServiceConnection provideStompServiceConnection(){
//        return new StompServiceConnection();
//    }


}
