package com.apap.director.client.data.util;

import android.app.Service;
import android.os.Binder;

public class SimpleBinder extends Binder {

    private Service service;

    public SimpleBinder(Service service) {
        this.service = service;
    }

    public Service getService() {
        return service;
    }
}
