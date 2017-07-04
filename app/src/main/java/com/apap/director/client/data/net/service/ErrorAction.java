package com.apap.director.client.data.net.service;


import android.util.Log;
import android.widget.Toast;

import com.apap.director.client.App;

import rx.functions.Action1;

class ErrorAction implements Action1<Throwable> {
    @Override
    public void call(Throwable throwable) {

        Toast.makeText(App.getContext(), throwable.getMessage(), Toast.LENGTH_LONG).show();
        Log.e("HAI/ErrorAction", "Error: " + throwable.getMessage(), throwable);
    }
}
