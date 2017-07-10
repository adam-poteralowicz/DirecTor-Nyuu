package com.apap.director.client.data.net.service;

import com.apap.director.client.presentation.ui.common.view.NetActivity;

import info.guardianproject.netcipher.client.StrongOkHttpClientBuilder;
import okhttp3.OkHttpClient;

/**
 * Created by Alicja Michniewicz
 */

public class HttpService {

    //Todo: implement
    private OkHttpClient okHttpClient;

    public void setUpOkHttpClient(NetActivity netActivity) throws Exception {
        StrongOkHttpClientBuilder.forMaxSecurity(netActivity.getApplicationContext())
                .withSocksProxy()
                .withTorValidation()
                .build(netActivity);
    }
}