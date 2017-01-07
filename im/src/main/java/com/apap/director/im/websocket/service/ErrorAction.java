package com.apap.director.im.websocket.service;


import android.util.Log;

import rx.functions.Action1;

public class ErrorAction implements Action1<Throwable> {
    @Override
    public void call(Throwable throwable) {

        //TODO: notify user that something went wrong

        Log.e("HAI/ErrorAction", "Error: "+ throwable.getMessage(), throwable);

    }
}
