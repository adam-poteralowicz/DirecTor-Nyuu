package com.apap.director.signal.module;

import android.content.Context;

import com.apap.director.manager.AccountManager;
import com.apap.director.signal.DirectorIdentityKeyStore;
import com.apap.director.signal.DirectorPreKeyStore;
import com.apap.director.signal.DirectorSessionStore;
import com.apap.director.signal.DirectorSignedPreKeyStore;

import org.whispersystems.curve25519.Curve25519;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;

@Module
public class SignalModule {

    private Context context;

    public SignalModule(Context context) {
        this.context = context;
    }

    @Provides
    @Singleton
    Curve25519 provideCurve25519() {
        return Curve25519.getInstance(Curve25519.BEST);
    }

    @Provides
    @Singleton
    DirectorIdentityKeyStore provideIdentityKeyStore(Realm realm, AccountManager manager) {
        return new DirectorIdentityKeyStore(realm, manager);
    }

    @Provides
    @Singleton
    DirectorPreKeyStore providePreKeyStore(Realm realm) {
        return new DirectorPreKeyStore(realm);
    }

    @Provides
    @Singleton
    DirectorSessionStore provideSessionStore(Realm realm) {
        return new DirectorSessionStore(realm);
    }

    @Provides
    @Singleton
    DirectorSignedPreKeyStore provideSignedPreKeyStore(Realm realm) {
        return new DirectorSignedPreKeyStore(realm);
    }

}
