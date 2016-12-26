package com.apap.director.im.signal.module;

import android.content.Context;

import com.apap.director.db.manager.DatabaseManager;
import com.apap.director.im.signal.DirectorIdentityKeyStore;
import com.apap.director.im.signal.DirectorPreKeyStore;
import com.apap.director.im.signal.DirectorSessionStore;
import com.apap.director.im.signal.DirectorSignedPreKeyStore;

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
    public DirectorPreKeyStore providePreKeyStore(){
        return new DirectorPreKeyStore();
    }

    @Provides
    @ApplicationScope
    public DirectorSessionStore provideSessionStore(){
        return new DirectorSessionStore();
    }

    @Provides
    @ApplicationScope
    public DirectorSignedPreKeyStore provideSignedPreKeyStore(){
        return new DirectorSignedPreKeyStore();
    }

}
