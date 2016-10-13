package com.apap.director.client;

import android.app.Application;
import android.content.Context;

import com.apap.director.client.component.ChatComponent;
import com.apap.director.client.component.DaggerChatComponent;
import com.apap.director.client.component.DaggerDaoComponent;
import com.apap.director.client.component.DaoComponent;
import com.apap.director.im.dao.module.DaoModule;
import com.apap.director.im.domain.chat.module.ChatModule;
import com.apap.director.im.domain.connection.module.ConnectionModule;
import com.apap.director.im.domain.message.module.MessageModule;

public class App extends Application {

        private static Context mContext;
        private ChatComponent chatComponent;
        private DaoComponent daoComponent;

        @Override
        public void onCreate() {
            super.onCreate();
            mContext = App.this;

            daoComponent = DaggerDaoComponent.builder()
                    .daoModule(new DaoModule(this))
                    .build();

            chatComponent = DaggerChatComponent.builder()
                    .connectionModule(new ConnectionModule())
                    .chatModule(new ChatModule())
                    .messageModule(new MessageModule())
                    .build();
        }

        public ChatComponent getChatComponent() {
            return chatComponent;
        }
        public DaoComponent getDaoComponent() { return daoComponent; }
        public static Context getContext(){
            return mContext;
        }

    }
