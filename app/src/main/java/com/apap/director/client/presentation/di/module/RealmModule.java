package com.apap.director.client.presentation.di.module;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;

@Module
public class RealmModule {

    @Provides
    @Singleton
    Realm provideRealm(Context context){
        Realm.init(context);
        Realm realm = Realm.getDefaultInstance();
        realm.setAutoRefresh(true);
        return realm;
    }
}
