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

@Module
public class SignalModule {

    private Context context;

    public SignalModule(Context context){
        this.context = context;
    }

    @Provides
    @ApplicationScope
    public DirectorIdentityKeyStore provideIdentityKeyStore(DatabaseManager databaseManager){
        return new DirectorIdentityKeyStore(context, databaseManager);
    }

    @Provides
    @ApplicationScope
    public DirectorPreKeyStore providePreKeyStore(DatabaseManager databaseManager){
        return new DirectorPreKeyStore(databaseManager);
    }

    @Provides
    @ApplicationScope
    public DirectorSessionStore provideSessionStore(DatabaseManager databaseManager){
        return new DirectorSessionStore(databaseManager);
    }

    @Provides
    @ApplicationScope
    public DirectorSignedPreKeyStore provideSignedPreKeyStore(DatabaseManager databaseManager){
        return new DirectorSignedPreKeyStore(databaseManager);
    }

    @Provides
    @ApplicationScope
    public StompService stompService(MessageAction messageAction){
        return new StompService(messageAction);
    }


    @Provides
    public MessageAction messageAction(DatabaseManager manager, DirectorPreKeyStore preKeyStore, DirectorSessionStore sessionStore, DirectorIdentityKeyStore identityKeyStore, DirectorSignedPreKeyStore signedPreKeyStore) {
        return new MessageAction(manager, preKeyStore, identityKeyStore, sessionStore, signedPreKeyStore);
    }
}
