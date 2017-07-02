package com.apap.director.client.presentation.ui.common.view;

import android.app.Activity;

import info.guardianproject.netcipher.client.StrongOkHttpClientBuilder;
import okhttp3.OkHttpClient;

/**
 * Created by Alicja Michniewicz on 03/07/2017.
 */

public class NetActivity extends Activity implements StrongOkHttpClientBuilder.Callback<OkHttpClient> {

    //Todo: snackbars on exception, success info on connection

    @Override
    public void onConnected(OkHttpClient client) {

    }

    @Override
    public void onConnectionException(Exception e) {

    }

    @Override
    public void onTimeout() {

    }

    @Override
    public void onInvalid() {

    }
}
