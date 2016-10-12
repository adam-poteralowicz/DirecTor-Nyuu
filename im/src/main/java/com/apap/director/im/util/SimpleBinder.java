package com.apap.director.im.util;

import android.app.Service;
import android.os.Binder;

public class SimpleBinder extends Binder {

    Service service;

    public SimpleBinder(Service service){
        this.service = service;
    }

    public Service getService() {
        return service;
    }
}
