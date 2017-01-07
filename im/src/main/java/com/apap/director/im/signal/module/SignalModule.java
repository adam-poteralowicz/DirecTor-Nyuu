package com.apap.director.im.signal.module;

import android.content.Context;

import com.apap.director.im.signal.DirectorIdentityKeyStore;
import com.apap.director.im.signal.DirectorPreKeyStore;
import com.apap.director.im.signal.DirectorSessionStore;
import com.apap.director.im.signal.DirectorSignedPreKeyStore;
import com.apap.director.manager.AccountManager;

import org.whispersystems.curve25519.Curve25519;

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
    @Singleton
    public Curve25519 provideCurve25519(){
        return Curve25519.getInstance(Curve25519.BEST);
    }

    @Provides
    @Singleton
    public DirectorIdentityKeyStore provideIdentityKeyStore(Realm realm, AccountManager manager){
        return new DirectorIdentityKeyStore(realm, manager);
    }

    @Provides
    @Singleton
    public DirectorPreKeyStore providePreKeyStore(Realm realm){
        return new DirectorPreKeyStore(realm);
    }

    @Provides
    @Singleton
    public DirectorSessionStore provideSessionStore(Realm realm){
        return new DirectorSessionStore(realm);
    }

    @Provides
    @Singleton
    public DirectorSignedPreKeyStore provideSignedPreKeyStore(Realm realm){
        return new DirectorSignedPreKeyStore(realm);
    }

}
