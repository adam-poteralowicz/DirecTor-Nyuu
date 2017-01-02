package com.apap.director.db.account;

import com.apap.director.db.rest.service.UserService;

import org.whispersystems.curve25519.Curve25519;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;

@Module
public class AccountModule {

    @Provides
    @Singleton
    AccountManager provideAccountManager(Realm realm, UserService userService, Curve25519 curve25519){
        return new AccountManager(realm, userService, curve25519);
    }

}
