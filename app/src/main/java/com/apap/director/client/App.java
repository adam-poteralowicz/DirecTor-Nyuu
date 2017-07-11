package com.apap.director.client;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;

import com.apap.director.client.presentation.di.component.DaggerMainComponent;
import com.apap.director.client.presentation.di.component.MainComponent;
import com.apap.director.client.presentation.di.module.ContextModule;
import com.apap.director.client.presentation.di.module.ManagerModule;
import com.apap.director.client.presentation.di.module.NetModule;
import com.apap.director.client.presentation.di.module.RealmModule;
import com.apap.director.client.presentation.di.module.RepositoryModule;
import com.apap.director.client.presentation.di.module.SignalModule;
import com.apap.director.client.presentation.ui.error.ErrorActivity;
import com.apap.director.client.presentation.ui.login.LoginActivity;
import com.apap.director.client.presentation.ui.password.PasswordActivity;

import butterknife.BindView;
import info.guardianproject.netcipher.client.StrongOkHttpClientBuilder;
import info.guardianproject.netcipher.proxy.OrbotHelper;
import io.realm.Realm;
import okhttp3.OkHttpClient;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class App extends Application implements StrongOkHttpClientBuilder.Callback<OkHttpClient> {

    private static final String SHARED_PREFERENCES_NAME = "prefs";

    @BindView(R.id.splashActivity_layout)
    View rootView;

    private static Context mContext;
    private MainComponent mainComponent;
    private SharedPreferences prefs;


    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(App.class.getSimpleName(), "App is starting...");
        mContext = App.this;
        Realm.init(this);
        prefs = getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE);

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

        String masterPassword =  prefs.getString("masterPassword", null);

        if(masterPassword != null) {
            Log.v(App.class.getSimpleName(), "Master password already set");

            Intent loginActivity = new Intent(this, LoginActivity.class);
            loginActivity.addFlags(FLAG_ACTIVITY_NEW_TASK);
            startActivity(loginActivity);
        }
        else {
            Log.v(App.class.getSimpleName(), "Master password needs to be set");

            Intent passwordActivity = new Intent(this, PasswordActivity.class);
            passwordActivity.addFlags(FLAG_ACTIVITY_NEW_TASK);
            startActivity(passwordActivity);
        }

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
        Log.e(App.class.getSimpleName(), "OkHttpClient timeout" );
    }

    @Override
    public void onInvalid() {
        Log.e(App.class.getSimpleName(), "OkHttpClient invalid" );
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
        Log.v("App", "Setting up injection");
        mainComponent = DaggerMainComponent.builder()
                .managerModule(new ManagerModule())
                .realmModule(new RealmModule())
                .signalModule(new SignalModule())
                .contextModule(new ContextModule(this))
                .repositoryModule(new RepositoryModule())
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
        if(!OrbotHelper.isOrbotInstalled(this)) {
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
