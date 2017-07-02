package com.apap.director.client;

import android.app.Application;
import android.content.Context;

import com.apap.director.client.presentation.di.component.DaggerMainComponent;
import com.apap.director.client.presentation.di.component.MainComponent;
import com.apap.director.client.data.net.websocket.module.WebSocketModule;
import com.apap.director.client.data.net.websocket.service.ClientService;
import com.apap.director.client.data.net.rest.module.RestModule;
import com.apap.director.client.data.store.module.SignalModule;
import com.apap.director.client.presentation.di.module.ManagerModule;
import com.apap.director.client.presentation.di.module.RealmModule;

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

            mainComponent = DaggerMainComponent.builder()
                    .managerModule(new ManagerModule())
                    .realmModule(new RealmModule(this))
                    .restModule(new RestModule())
                    .signalModule(new SignalModule(this))
                    .webSocketModule(new WebSocketModule(this))
                    .build();

            ClientService.init(mainComponent.getMessageAction(), mainComponent.getDirectorSessionStore(), mainComponent.getDirectorIdentityKeyStore(), mainComponent.getDirectorPreKeyStore(), mainComponent.getDirectorSignedPreKeyStore(), mainComponent.getKeyService());

            OrbotHelper.get(this).init();
            //OrbotHelper.requestStartTor(this);
            OrbotHelper.get(this).requestStatus(this);
        }

        public MainComponent getComponent() { return mainComponent; }

        public static Context getContext(){
            return mContext;
        }
    }
