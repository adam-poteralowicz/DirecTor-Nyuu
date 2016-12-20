package com.apap.director.im.signal.module;

import android.content.Context;

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
    @Singleton
    public DirectorIdentityKeyStore provideIdentityKeyStore(){
        return new DirectorIdentityKeyStore(context);
    }

    @Provides
    @Singleton
    public DirectorPreKeyStore providePreKeyStore(){
        return new DirectorPreKeyStore();
    }

    @Provides
    @Singleton
    public DirectorSessionStore provideSessionStore(){
        return new DirectorSessionStore();
    }

    @Provides
    @Singleton
    public DirectorSignedPreKeyStore provideSignedPreKeyStore(){
        return new DirectorSignedPreKeyStore();
    }

}
