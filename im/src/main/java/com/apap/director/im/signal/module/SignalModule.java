package com.apap.director.im.signal.module;

import android.content.Context;

import com.apap.director.db.manager.DatabaseManager;
import com.apap.director.im.signal.DirectorIdentityKeyStore;
import com.apap.director.im.signal.DirectorPreKeyStore;
import com.apap.director.im.signal.DirectorSessionStore;
import com.apap.director.im.signal.DirectorSignedPreKeyStore;
import com.apap.director.im.websocket.service.MessageAction;
import com.apap.director.im.websocket.service.StompService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;

@Module
public class SignalModule {

    private Context context;

    public SignalModule(Context context){
        this.context = context;
    }

    @Provides
    @ApplicationScope
    public DirectorIdentityKeyStore provideIdentityKeyStore(){
        return new DirectorIdentityKeyStore(context, Realm.getDefaultInstance());
    }

    @Provides
    @ApplicationScope
    public DirectorPreKeyStore providePreKeyStore(){
        return new DirectorPreKeyStore(Realm.getDefaultInstance());
    }

    @Provides
    @ApplicationScope
    public DirectorSessionStore provideSessionStore(){
        return new DirectorSessionStore(Realm.getDefaultInstance());
    }

    @Provides
    @ApplicationScope
    public DirectorSignedPreKeyStore provideSignedPreKeyStore(){
        return new DirectorSignedPreKeyStore(Realm.getDefaultInstance());
    }

    @Provides
    @ApplicationScope
    public StompService stompService(MessageAction messageAction){
        return new StompService(messageAction);
    }


    @Provides
    public MessageAction messageAction(DirectorPreKeyStore preKeyStore, DirectorSessionStore sessionStore, DirectorIdentityKeyStore identityKeyStore, DirectorSignedPreKeyStore signedPreKeyStore) {
        return new MessageAction(preKeyStore, identityKeyStore, sessionStore, signedPreKeyStore);
    }
}
