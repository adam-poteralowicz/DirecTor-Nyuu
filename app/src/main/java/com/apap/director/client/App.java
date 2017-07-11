package com.apap.director.client;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.apap.director.client.activity.ErrorActivity;
import com.apap.director.client.activity.LoginActivity;
import com.apap.director.client.component.DaggerMainComponent;
import com.apap.director.client.component.MainComponent;
import com.apap.director.db.realm.module.RealmModule;
import com.apap.director.im.websocket.module.WebSocketModule;
import com.apap.director.manager.ManagerModule;
import com.apap.director.net.NetModule;
import com.apap.director.network.rest.module.RestModule;
import com.apap.director.signal.module.SignalModule;

import info.guardianproject.netcipher.client.StrongOkHttpClientBuilder;
import info.guardianproject.netcipher.proxy.OrbotHelper;
import io.realm.Realm;
import okhttp3.OkHttpClient;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class App extends Application implements StrongOkHttpClientBuilder.Callback<OkHttpClient> {

    private static Context mContext;
    private MainComponent mainComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(App.class.getSimpleName(), "App is starting...");
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
        Log.v(App.class.getSimpleName(), "OkHttpClient connected");
        Log.v(App.class.getSimpleName(), "APP CONNECTED");
        setUpInjection(okHttpClient);

        Intent loginActivity = new Intent(this, LoginActivity.class);
        loginActivity.addFlags(FLAG_ACTIVITY_NEW_TASK);
        startActivity(loginActivity);
    }

    @Override
    public void onConnectionException(Exception e) {
        Log.e(App.class.getSimpleName(), "OkHttpClient exception", e);
        Intent errorActivity = new Intent(this, ErrorActivity.class).putExtra("error", "OkHttpClient exception: " + e.getMessage());
        errorActivity.addFlags(FLAG_ACTIVITY_NEW_TASK);
        startActivity(errorActivity);
    }

    @Override
    public void onTimeout() {
        Log.e(App.class.getSimpleName(), "OkHttpClient timeout");
    }

    @Override
    public void onInvalid() {
        Log.e(App.class.getSimpleName(), "OkHttpClient invalid");
    }

    private void initClient() {
        try {
            initOkHttp3Client();
        } catch (Exception ex) {
            Log.e(App.class.getSimpleName(), "Initializing OkHttpClient failed", ex);
        }
    }

    private void setUpInjection(OkHttpClient client) {
        Log.v("App", "Setting up injection");
        mainComponent = DaggerMainComponent.builder()
                .managerModule(new ManagerModule())
                .realmModule(new RealmModule(this))
                .restModule(new RestModule())
                .signalModule(new SignalModule(this))
                .webSocketModule(new WebSocketModule(this))
                .netModule(new NetModule(client))
                .build();
        Log.v("App", "Finished setting up injection");
    }

    private void initOrbot() {
        Log.d(App.class.getSimpleName(), "Initializing orbot...");

        sanityCheckOrbot();

        OrbotHelper.get(this).init();
        OrbotHelper.requestStartTor(this);
        OrbotHelper.get(this).requestStatus(this);
    }

    private void sanityCheckOrbot() {
        if (!OrbotHelper.isOrbotInstalled(this)) {
            Log.v(App.class.getSimpleName(), "Orbot not installed");
            startActivity(new Intent(this, ErrorActivity.class).putExtra("error", "Orbot not installed").setFlags(FLAG_ACTIVITY_NEW_TASK));
        }
    }

    private void initOkHttp3Client() throws Exception {
        Log.d(App.class.getSimpleName(), "Initializing client...");

        StrongOkHttpClientBuilder
                .forMaxSecurity(this)
                .withTorValidation()
                .build(this);
    }
}
