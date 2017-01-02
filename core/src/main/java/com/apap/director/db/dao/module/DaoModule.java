package com.apap.director.db.dao.module;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;

@Module
public class DaoModule {

    Context context;

    public DaoModule(Context context){
        this.context = context;
    }

//    @Provides
//    @Singleton
//    public DatabaseManager provideDatabaseManager(){
//        return new DatabaseManager(context);
//    }


    @Provides
    @Singleton
    public Realm provideRealm(){
        return Realm.getDefaultInstance();
    }

}
