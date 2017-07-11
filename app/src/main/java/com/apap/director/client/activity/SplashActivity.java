package com.apap.director.client.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.apap.director.client.R;

/**
 * Created by Adam Poterałowicz
 */

public class SplashActivity extends Activity {
    private static final int LAYOUT_ID = R.layout.splash_activity_layout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT_ID);

        startActivity(new Intent(this, LoginActivity.class));
    }
}
