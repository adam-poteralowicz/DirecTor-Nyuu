package com.apap.director.client.data.net.service;


import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;

import javax.inject.Inject;

import rx.functions.Action1;

class ErrorAction implements Action1<Throwable> {

    private View view;

    @Inject
    ErrorAction(View view) {
        this.view = view;
    }

    @Override
    public void call(Throwable throwable) {

        Snackbar.make(view, throwable.getMessage(), Snackbar.LENGTH_LONG).show();
        Log.e("HAI/ErrorAction", "Error: " + throwable.getMessage(), throwable);
    }
}
