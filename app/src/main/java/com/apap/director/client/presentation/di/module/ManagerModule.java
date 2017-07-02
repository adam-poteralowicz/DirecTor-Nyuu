package com.apap.director.client.presentation.di.module;

import com.apap.director.client.data.manager.AccountManager;
import com.apap.director.client.data.manager.ContactManager;
import com.apap.director.client.data.manager.ConversationManager;
import com.apap.director.client.data.manager.MessageManager;
import com.apap.director.client.data.net.rest.service.KeyService;
import com.apap.director.client.data.net.rest.service.UserService;
import com.apap.director.client.data.store.DirectorPreKeyStore;
import com.apap.director.client.data.store.DirectorSignedPreKeyStore;

import org.whispersystems.curve25519.Curve25519;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;

@Module
public class ManagerModule {

    @Provides
    @Singleton
    AccountManager provideAccountManager(Realm realm, UserService userService, Curve25519 curve25519, KeyService keyService, DirectorPreKeyStore preKeyStore, DirectorSignedPreKeyStore signedPreKeyStore) {
        return new AccountManager(realm, userService, curve25519, keyService, preKeyStore, signedPreKeyStore);
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
