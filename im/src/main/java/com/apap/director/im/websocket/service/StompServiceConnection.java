//package com.apap.director.im.websocket.service;
//
//import android.content.ComponentName;
//import android.content.ServiceConnection;
//import android.os.IBinder;
//import android.util.Log;
//
//import com.apap.director.im.util.SimpleBinder;
//
//public class StompServiceConnection implements ServiceConnection {
//
//    StompService stompService;
//
//    @Override
//    public void onServiceConnected(ComponentName name, IBinder service) {
//        SimpleBinder binder = (SimpleBinder) service;
//        stompService = (StompService) binder.getService();
//        Log.v("HAI/ServiceConnection", "Connected");
//    }
//
//    @Override
//    public void onServiceDisconnected(ComponentName name) {
//        Log.v("HAIServiceConnection", "Disonnected");
//    }
//
//}
