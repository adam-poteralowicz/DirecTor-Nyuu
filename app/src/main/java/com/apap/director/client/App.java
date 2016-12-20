package com.apap.director.client;

import android.app.Application;
import android.content.Context;

import com.apap.director.client.component.DaggerDaoComponent;
import com.apap.director.client.component.DaggerSignalComponent;
import com.apap.director.client.component.DaoComponent;
import com.apap.director.client.component.SignalComponent;
import com.apap.director.db.dao.module.DaoModule;
import com.apap.director.im.domain.websocket.module.WebSocketModule;
import com.apap.director.im.signal.module.SignalModule;

public class App extends Application {

        private static Context mContext;
        private SignalComponent signalComponent;
        private DaoComponent daoComponent;

        @Override
        public void onCreate() {
            super.onCreate();
            mContext = App.this;


            signalComponent = DaggerSignalComponent.builder()
                    .signalModule(new SignalModule(this))
                    .webSocketModule(new WebSocketModule())
                    .build();

            daoComponent = DaggerDaoComponent.builder()
                    .daoModule(new DaoModule(this))
                    .build();


        }


        public SignalComponent getSignalComponent() { return signalComponent; }
        public DaoComponent getDaoComponent() { return daoComponent; }
        public static Context getContext(){
            return mContext;
        }

    }
