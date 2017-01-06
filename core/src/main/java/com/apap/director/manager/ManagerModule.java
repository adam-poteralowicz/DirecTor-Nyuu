package com.apap.director.manager;

import com.apap.director.db.dao.model.Conversation;
import com.apap.director.network.rest.service.KeyService;
import com.apap.director.network.rest.service.UserService;
import com.fasterxml.jackson.databind.annotation.JsonAppend;

import org.whispersystems.curve25519.Curve25519;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;

@Module
public class ManagerModule {

    @Provides
    @Singleton
    AccountManager provideAccountManager(Realm realm, UserService userService, Curve25519 curve25519){
        return new AccountManager(realm, userService, curve25519);
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
