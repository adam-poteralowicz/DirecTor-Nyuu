package com.apap.director.client.presentation.di.module;

import com.apap.director.client.data.manager.AccountManager;
import com.apap.director.client.data.manager.ContactManager;
import com.apap.director.client.data.manager.MessageManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;

@Module
public class ManagerModule {

    @Provides
    @Singleton
    AccountManager provideAccountManager(Realm realm) {
        return new AccountManager(realm);
    }

    @Provides
    @Singleton
    ContactManager provideContactManager(Realm realm) {
        return new ContactManager(realm);
    }

    @Provides
    @Singleton
    MessageManager provideMessageManager(Realm realm) {
        return new MessageManager(realm);
    }
}
