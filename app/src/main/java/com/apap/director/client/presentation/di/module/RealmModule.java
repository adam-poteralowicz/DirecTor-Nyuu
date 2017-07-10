package com.apap.director.client.presentation.di.module;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;
import io.realm.RealmConfiguration;

@Module
public class RealmModule {

    @Provides
    @Singleton
    Realm provideRealm(){
        RealmConfiguration configuration = new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build();

        Realm realm = Realm.getInstance(configuration);
        realm.setAutoRefresh(true);
        return realm;
    }
}
