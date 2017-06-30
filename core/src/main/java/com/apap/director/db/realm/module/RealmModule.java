package com.apap.director.db.realm.module;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;

@Module
public class RealmModule {
    Context context;

    public RealmModule(Context context){
        this.context = context;
    }

    @Provides
    @Singleton
    Realm provideRealm(){
        Realm realm = Realm.getDefaultInstance();
        realm.setAutoRefresh(true);
        return realm;
    }
}
