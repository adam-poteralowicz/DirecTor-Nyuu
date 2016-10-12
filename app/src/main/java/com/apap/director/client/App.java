package com.apap.director.client;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.apap.director.client.dao.model.DaoMaster;
import com.apap.director.client.dao.model.DaoSession;
import com.apap.director.im.dagger.ChatComponent;
import com.apap.director.im.dagger.DaggerChatComponent;
import com.apap.director.im.domain.chat.module.ChatModule;
import com.apap.director.im.domain.connection.module.ConnectionModule;

public class App extends Application {

        public DaoSession contactDaoSession, conversationDaoSession, messageDaoSession;
        private static Context mContext;

        @Override
        public void onCreate() {
            super.onCreate();
            mContext = App.this;

            createContactDaoSession();
            createConversationDaoSession();
            createMessageDaoSession();

            ChatComponent chatComponent = DaggerChatComponent.builder()
                    .chatModule(new ChatModule())
                    .connectionModule(new ConnectionModule())
                    .build();
        }

        public static Context getContext(){
            return mContext;
        }
        public DaoSession getContactDaoSession() {
            return contactDaoSession;
        }
        public DaoSession getConversationDaoSession() { return conversationDaoSession; }
        public DaoSession getMessageDaoSession() { return messageDaoSession; }

        private void createContactDaoSession() {
            DaoMaster.DevOpenHelper contactsHelper = new DaoMaster.DevOpenHelper(this, "contact-db", null);
            SQLiteDatabase contact_db = contactsHelper.getWritableDatabase();
            DaoMaster contactDaoMaster = new DaoMaster(contact_db);
            contactDaoSession = contactDaoMaster.newSession();
        }

        private void createConversationDaoSession() {
            DaoMaster.DevOpenHelper inboxHelper = new DaoMaster.DevOpenHelper(this, "conversation-db", null);
            SQLiteDatabase conversation_db = inboxHelper.getWritableDatabase();
            DaoMaster conversationDaoMaster = new DaoMaster(conversation_db);
            conversationDaoSession = conversationDaoMaster.newSession();
        }

        private void createMessageDaoSession() {
            DaoMaster.DevOpenHelper messageHelper = new DaoMaster.DevOpenHelper(this, "message-db", null);
            SQLiteDatabase message_db = messageHelper.getWritableDatabase();
            DaoMaster messageDaoMaster = new DaoMaster(message_db);
            messageDaoSession = messageDaoMaster.newSession();
        }
    }
