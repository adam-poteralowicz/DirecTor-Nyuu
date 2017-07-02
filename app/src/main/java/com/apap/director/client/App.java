package com.apap.director.client;

import android.app.Application;
import android.content.Context;

import com.apap.director.client.data.net.service.ClientService;
import com.apap.director.client.presentation.di.component.DaggerMainComponent;
import com.apap.director.client.presentation.di.component.MainComponent;
import com.apap.director.client.presentation.di.module.ManagerModule;
import com.apap.director.client.presentation.di.module.RealmModule;
import com.apap.director.client.presentation.di.module.SignalModule;

import info.guardianproject.netcipher.proxy.OrbotHelper;
import io.realm.Realm;
import okhttp3.OkHttpClient;

public class App extends Application {

    private static Context mContext;
    private MainComponent mainComponent;
    private OkHttpClient client;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = App.this;
        Realm.init(this);


        OrbotHelper.get(this).init();
        //OrbotHelper.requestStartTor(this);
        OrbotHelper.get(this).requestStatus(this);
    }

    public MainComponent getComponent() {
        return mainComponent;
    }

    public static Context getContext() {
        return mContext;
    }

    private void setUpInjection() {
        mainComponent = DaggerMainComponent.builder()
                .managerModule(new ManagerModule())
                .realmModule(new RealmModule())
                .signalModule(new SignalModule())
                .build();

        ClientService.init(mainComponent.getMessageAction(), mainComponent.getDirectorSessionStore(), mainComponent.getDirectorIdentityKeyStore(), mainComponent.getDirectorPreKeyStore(), mainComponent.getDirectorSignedPreKeyStore(), mainComponent.getKeyService());
    }


}
