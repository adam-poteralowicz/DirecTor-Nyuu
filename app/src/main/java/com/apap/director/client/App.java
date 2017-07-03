package com.apap.director.client;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.apap.director.client.data.net.service.ClientService;
import com.apap.director.client.presentation.di.component.DaggerMainComponent;
import com.apap.director.client.presentation.di.component.MainComponent;
import com.apap.director.client.presentation.di.module.ContextModule;
import com.apap.director.client.presentation.di.module.ManagerModule;
import com.apap.director.client.presentation.di.module.NetModule;
import com.apap.director.client.presentation.di.module.RealmModule;
import com.apap.director.client.presentation.di.module.RepositoryModule;
import com.apap.director.client.presentation.di.module.SignalModule;
import com.mobsandgeeks.saripaar.annotation.Or;

import info.guardianproject.netcipher.client.StrongOkHttpClientBuilder;
import info.guardianproject.netcipher.proxy.OrbotHelper;
import io.realm.Realm;
import okhttp3.OkHttpClient;

public class App extends Application implements StrongOkHttpClientBuilder.Callback<OkHttpClient> {

    private static Context mContext;
    private MainComponent mainComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = App.this;
        Realm.init(this);

        initOrbot();
        initClient();

    }

    public MainComponent getComponent() {
        return mainComponent;
    }

    public static Context getContext() {
        return mContext;
    }

    @Override
    public void onConnected(OkHttpClient okHttpClient) {
        setUpInjection(okHttpClient);
    }

    //TODO: Send user to Error Activity, tell him Orbot's broken, meow :(
    @Override
    public void onConnectionException(Exception e) {

    }

    @Override
    public void onTimeout() {

    }

    @Override
    public void onInvalid() {

    }

    private void initClient() {
        try {
            initOkHttp3Client();
        }
        catch (Exception ex) {
            Log.e(App.class.getSimpleName(), "Initializing OkHttpClient failed", ex);
        }
    }

    private void setUpInjection(OkHttpClient client) {
        mainComponent = DaggerMainComponent.builder()
                .managerModule(new ManagerModule())
                .realmModule(new RealmModule())
                .signalModule(new SignalModule())
                .contextModule(new ContextModule(this))
                .repositoryModule(new RepositoryModule())
                .netModule(new NetModule(client))
                .build();

        ClientService.init(mainComponent.getMessageAction(), mainComponent.getDirectorSessionStore(), mainComponent.getDirectorIdentityKeyStore(), mainComponent.getDirectorPreKeyStore(), mainComponent.getDirectorSignedPreKeyStore(), mainComponent.getKeyService());
    }

    private void initOrbot() {
        OrbotHelper.get(this).init();
//        OrbotHelper.requestStartTor(this);
//        OrbotHelper.get(this).requestStatus(this);
    }

    private void initOkHttp3Client() throws Exception {
        StrongOkHttpClientBuilder
                .forMaxSecurity(this)
                .withTorValidation()
                .withSocksProxy()
                .build(this);
    }
}
