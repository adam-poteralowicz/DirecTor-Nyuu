package com.apap.director.client.presentation.di.module;

import com.apap.director.client.data.manager.AccountManager;
import com.apap.director.client.data.manager.ContactManager;
import com.apap.director.client.data.manager.ConversationManager;
import com.apap.director.client.data.manager.MessageManager;
import com.apap.director.client.data.net.rest.service.KeyService;
import com.apap.director.client.data.net.rest.service.RestAccountService;
import com.apap.director.client.data.store.PreKeyStoreImpl;
import com.apap.director.client.data.store.SignedPreKeyStoreImpl;

import org.whispersystems.curve25519.Curve25519;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;

@Module
public class ManagerModule {

    @Provides
    @Singleton
    AccountManager provideAccountManager(Realm realm, RestAccountService restAccountService, Curve25519 curve25519, KeyService keyService, PreKeyStoreImpl preKeyStore, SignedPreKeyStoreImpl signedPreKeyStore) {
        return new AccountManager(realm, restAccountService, curve25519, keyService, preKeyStore, signedPreKeyStore);
    }

    @Provides
    @Singleton
    ContactManager provideContactManager(Realm realm, AccountManager manager, KeyService keyService) {
        return new ContactManager(realm, manager, keyService);
    }

    @Provides
    @Singleton
    MessageManager provideMessageManager(Realm realm, AccountManager manager) {
        return new MessageManager(realm, manager);
    }

    @Provides
    @Singleton
    ConversationManager provideConversationManager(Realm realm, AccountManager manager) {
        return new ConversationManager(realm, manager);
    }

}
