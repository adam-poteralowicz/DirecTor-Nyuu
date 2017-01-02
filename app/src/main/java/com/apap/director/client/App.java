package com.apap.director.client;

import android.app.Application;
import android.content.Context;

import com.apap.director.client.component.DaggerMainComponent;
import com.apap.director.client.component.MainComponent;
import com.apap.director.db.account.AccountModule;
import com.apap.director.db.dao.module.DaoModule;
import com.apap.director.db.rest.module.RestModule;
import com.apap.director.im.signal.module.SignalModule;

import info.guardianproject.netcipher.proxy.OrbotHelper;
import io.realm.Realm;

public class App extends Application {

        private static Context mContext;
        private MainComponent mainComponent;

        @Override
        public void onCreate() {
            super.onCreate();
            mContext = App.this;

            Realm.init(this);
//
//            daoComponent = DaggerDaoComponent.builder()
//                    .daoModule(new DaoModule(this))
//                    .build();
//
//            signalComponent = DaggerSignalComponent.builder()
//                    .signalModule(new SignalModule(this))
//                    .webSocketModule(new WebSocketModule())
//                    .daoComponent(daoComponent)
//                    .build();

            mainComponent = DaggerMainComponent.builder()
                    .accountModule(new AccountModule())
                    .daoModule(new DaoModule(this))
                    .restModule(new RestModule())
                    .signalModule(new SignalModule(this))
                    .build();




            OrbotHelper.get(this).init();
            //OrbotHelper.requestStartTor(this);
            OrbotHelper.get(this).requestStatus(this);
        }


        public MainComponent getComponent() { return mainComponent; }
        public static Context getContext(){
            return mContext;
        }

    }
