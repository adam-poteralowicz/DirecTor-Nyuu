package com.apap.director.client.presentation.ui.splash;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.apap.director.client.R;
import com.apap.director.client.presentation.ui.login.LoginActivity;
import com.apap.director.client.presentation.ui.password.PasswordActivity;

/**
 * Created by Alicja Michniewicz
 */

public class SplashActivity extends Activity {

    private static final int LAYOUT_ID = R.layout.splash_activity_layout;
    private static final String SHARED_PREFERENCES_FILENAME = "prefs";
    private static final String KEY = "masterPassword";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT_ID);
    }
}
