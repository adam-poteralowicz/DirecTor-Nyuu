package com.apap.director.db.dao.module;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.apap.director.db.dao.model.DaoMaster;
import com.apap.director.db.dao.model.DaoSession;
import com.apap.director.db.manager.DatabaseManager;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DaoModule {

    Context context;

    public DaoModule(Context context){
        this.context = context;
    }

    @Provides
    @Singleton
    public DatabaseManager provideDatabaseManager(){
        return new DatabaseManager(context);
    }

}
