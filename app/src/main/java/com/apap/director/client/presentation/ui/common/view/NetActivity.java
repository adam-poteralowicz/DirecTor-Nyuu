package com.apap.director.client.presentation.ui.common.view;

import android.app.Activity;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.apap.director.client.R;

import butterknife.BindView;
import info.guardianproject.netcipher.client.StrongOkHttpClientBuilder;
import okhttp3.OkHttpClient;

/**
 * Created by Alicja Michniewicz
 */

public class NetActivity extends Activity implements StrongOkHttpClientBuilder.Callback<OkHttpClient> {

    @BindView(R.id.loginActivity_layout)
    View rootView;

    @Override
    public void onConnected(OkHttpClient client) {
        Snackbar.make(rootView, "Successfully connected", Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onConnectionException(Exception e) {
        Snackbar.make(rootView, e.getMessage(), Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onTimeout() {
        Snackbar.make(rootView, "Server connection timeout", Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onInvalid() {
        Snackbar.make(rootView, "Invalid connection URL", Snackbar.LENGTH_LONG).show();
    }
}
