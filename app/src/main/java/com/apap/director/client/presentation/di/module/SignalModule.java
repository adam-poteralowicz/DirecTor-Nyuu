package com.apap.director.client.presentation.di.module;

import com.apap.director.client.data.db.service.AccountStore;
import com.apap.director.client.data.store.IdentityKeyStoreImpl;
import com.apap.director.client.data.store.PreKeyStoreImpl;
import com.apap.director.client.data.store.SessionStoreImpl;
import com.apap.director.client.data.store.SignedPreKeyStoreImpl;

import org.whispersystems.curve25519.Curve25519;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;

@Module
public class SignalModule {

    @Provides
    @Singleton
    Curve25519 provideCurve25519() {
        return Curve25519.getInstance(Curve25519.BEST);
    }

    @Provides
    @Singleton
    IdentityKeyStoreImpl provideIdentityKeyStore(AccountStore accountStore) {
        return new IdentityKeyStoreImpl(accountStore);
    }

    @Provides
    @Singleton
    PreKeyStoreImpl providePreKeyStore(Realm realm) {
        return new PreKeyStoreImpl(realm);
    }

    @Provides
    @Singleton
    SessionStoreImpl provideSessionStore(Realm realm) {
        return new SessionStoreImpl(realm);
    }

    @Provides
    @Singleton
    SignedPreKeyStoreImpl provideSignedPreKeyStore(Realm realm) {
        return new SignedPreKeyStoreImpl(realm);
    }

}
