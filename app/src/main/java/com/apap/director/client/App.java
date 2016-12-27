package com.apap.director.client;

import android.app.Application;
import android.content.Context;


import com.apap.director.client.component.DaggerDaoComponent;
import com.apap.director.client.component.DaggerSignalComponent;
import com.apap.director.client.component.DaoComponent;
import com.apap.director.client.component.SignalComponent;
import com.apap.director.db.dao.module.DaoModule;
import com.apap.director.im.signal.module.SignalModule;
import com.apap.director.im.websocket.module.WebSocketModule;
import info.guardianproject.netcipher.proxy.OrbotHelper;

public class App extends Application {

        private static Context mContext;
        private SignalComponent signalComponent;
        private DaoComponent daoComponent;

        @Override
        public void onCreate() {
            super.onCreate();
            mContext = App.this;

            daoComponent = DaggerDaoComponent.builder()
                    .daoModule(new DaoModule(this))
                    .build();

            signalComponent = DaggerSignalComponent.builder()
                    .signalModule(new SignalModule(this))
                    .webSocketModule(new WebSocketModule())
                    .daoComponent(daoComponent)
                    .build();

            OrbotHelper.get(this).init();
            //OrbotHelper.requestStartTor(this);
            OrbotHelper.get(this).requestStatus(this);
        }

        public SignalComponent getSignalComponent() { return signalComponent; }
        public DaoComponent getDaoComponent() { return daoComponent; }
        public static Context getContext(){
            return mContext;
        }

    }
