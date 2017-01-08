package com.apap.director.client;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.apap.director.client.component.DaggerMainComponent;
import com.apap.director.client.component.MainComponent;
import com.apap.director.db.dao.module.DaoModule;
import com.apap.director.im.signal.module.SignalModule;
import com.apap.director.im.websocket.module.WebSocketModule;
import com.apap.director.im.websocket.service.ClientService;
import com.apap.director.manager.ManagerModule;
import com.apap.director.network.rest.module.RestModule;

import info.guardianproject.netcipher.client.StrongBuilder;
import info.guardianproject.netcipher.client.StrongOkHttpClientBuilder;
import info.guardianproject.netcipher.proxy.OrbotHelper;
import io.realm.Realm;
import okhttp3.OkHttpClient;

public class App extends Application implements StrongBuilder.Callback<OkHttpClient>  {

        private static Context mContext;
        private MainComponent mainComponent;


        @Override
        public void onCreate() {
            super.onCreate();
            mContext = App.this;

            Realm.init(this);

            mainComponent = DaggerMainComponent.builder()
                    .managerModule(new ManagerModule())
                    .daoModule(new DaoModule(this))
                    .restModule(new RestModule())
                    .signalModule(new SignalModule(this))
                    .webSocketModule(new WebSocketModule(this))
                    .build();


            ClientService.init(mainComponent.getMessageAction(), mainComponent.getDirectorSessionStore(), mainComponent.getDirectorIdentityKeyStore(), mainComponent.getDirectorPreKeyStore(), mainComponent.getDirectorSignedPreKeyStore(), mainComponent.getKeyService());

            OrbotHelper.get(this).init();
            OrbotHelper.get(this).requestStatus(this);

            StrongOkHttpClientBuilder builder = new StrongOkHttpClientBuilder(this);

            try {
                builder
                        .forMaxSecurity(this)
                        .withTorValidation()
                        .build(this);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        public MainComponent getComponent() { return mainComponent; }
        public static Context getContext(){
            return mContext;
        }

    @Override
    public void onConnected(OkHttpClient okHttpClient) {
        Log.v("HAI/App", "Proxy connection ok");

    }

    @Override
    public void onConnectionException(Exception e) {
        Log.e(getClass().getSimpleName(),
                "Exception connecting to hidden service", e);
    }

    @Override
    public void onTimeout() {
        Log.d("onTimeout", String.valueOf(R.string.msg_timeout));
    }

    @Override
    public void onInvalid() {
        Log.d("onInvalid", String.valueOf(R.string.msg_invalid));
    }
}
