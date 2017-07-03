package com.apap.director.client.presentation.ui.splash;

import android.app.Activity;
import android.os.Bundle;

import com.apap.director.client.R;

/**
 * Created by Alicja Michniewicz
 */

public class SplashActivity extends Activity {

    private final static int LAYOUT_ID = R.layout.splash_activity_layout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT_ID);
    }
    
}
