package com.apap.director.director_db.dao.module;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.apap.director.director_db.dao.model.DaoMaster;
import com.apap.director.director_db.dao.model.DaoSession;

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
    @Named("contactDao")
    public DaoSession createContactDaoSession() {
        DaoMaster.DevOpenHelper contactsHelper = new DaoMaster.DevOpenHelper(context, "contact-db", null);
        SQLiteDatabase contact_db = contactsHelper.getWritableDatabase();
        DaoMaster contactDaoMaster = new DaoMaster(contact_db);
        return contactDaoMaster.newSession();
    }

    @Provides
    @Singleton
    @Named("conversationDao")
    public DaoSession createConversationDaoSession() {
        DaoMaster.DevOpenHelper inboxHelper = new DaoMaster.DevOpenHelper(context, "conversation-db", null);
        SQLiteDatabase conversation_db = inboxHelper.getWritableDatabase();
        DaoMaster conversationDaoMaster = new DaoMaster(conversation_db);
        return conversationDaoMaster.newSession();
    }

    @Provides
    @Singleton
    @Named("messageDao")
    public DaoSession createMessageDaoSession() {
        DaoMaster.DevOpenHelper messageHelper = new DaoMaster.DevOpenHelper(context, "message-db", null);
        SQLiteDatabase message_db = messageHelper.getWritableDatabase();
        DaoMaster messageDaoMaster = new DaoMaster(message_db);
        return messageDaoMaster.newSession();
    }
}
