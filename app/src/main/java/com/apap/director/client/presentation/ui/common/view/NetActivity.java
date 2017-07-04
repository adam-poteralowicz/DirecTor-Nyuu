package com.apap.director.client.presentation.ui.common.view;

import android.app.Activity;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

import com.apap.director.client.R;

import butterknife.BindView;
import info.guardianproject.netcipher.client.StrongOkHttpClientBuilder;
import okhttp3.OkHttpClient;

/**
 * Created by Alicja Michniewicz on 03/07/2017.
 */

public class NetActivity extends Activity implements StrongOkHttpClientBuilder.Callback<OkHttpClient> {

    @BindView(R.id.loginActivity_layout)
    View rootView;

    @Override
    public void onConnected(OkHttpClient client) {
        Toast.makeText(this, "Successfully connected", Toast.LENGTH_LONG).show();
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
