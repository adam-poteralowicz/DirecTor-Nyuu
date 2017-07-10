package com.apap.director.client.presentation.di.module;

import com.apap.director.client.data.db.service.AccountStore;
import com.apap.director.client.data.store.IdentityKeyStoreImpl;
import com.apap.director.client.data.store.PreKeyStoreImpl;
import com.apap.director.client.data.store.SessionStoreImpl;
import com.apap.director.client.data.store.SignedPreKeyStoreImpl;
import com.apap.director.client.domain.util.EncryptionService;

import org.whispersystems.curve25519.Curve25519;
import org.whispersystems.libsignal.state.IdentityKeyStore;
import org.whispersystems.libsignal.state.PreKeyStore;
import org.whispersystems.libsignal.state.SessionStore;
import org.whispersystems.libsignal.state.SignedPreKeyStore;

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
    IdentityKeyStore provideIdentityKeyStore(Realm realm, AccountStore accountStore) {
        return new IdentityKeyStoreImpl(realm, accountStore);
    }

    @Provides
    @Singleton
    PreKeyStore providePreKeyStore(Realm realm) {
        return new PreKeyStoreImpl(realm);
    }

    @Provides
    @Singleton
    SessionStore provideSessionStore(Realm realm, AccountStore accountStore) {
        return new SessionStoreImpl(realm, accountStore);
    }

    @Provides
    @Singleton
    SignedPreKeyStore provideSignedPreKeyStore(Realm realm) {
        return new SignedPreKeyStoreImpl(realm);
    }

    @Provides
    @Singleton
    EncryptionService encryptionService(Curve25519 curve25519, IdentityKeyStore identityKeyStore, PreKeyStore preKeyStore, SessionStore sessionStore, SignedPreKeyStore signedPreKeyStore) {
        return new EncryptionService(curve25519, identityKeyStore, signedPreKeyStore, sessionStore, preKeyStore);
    }

}
