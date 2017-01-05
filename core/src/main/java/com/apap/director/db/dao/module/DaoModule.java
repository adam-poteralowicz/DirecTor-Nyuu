package com.apap.director.db.dao.module;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;
import io.realm.RealmChangeListener;

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
        Realm realm = Realm.getDefaultInstance();
//
//        realm.addChangeListener(new RealmChangeListener<Realm>() {
//            @Override
//            public void onChange(Realm element) {
//                this.realm = element;
//            }
//        });
        realm.setAutoRefresh(true);
        return realm;
    }

}
