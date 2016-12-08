package com.apap.director.im.domain.websocket.service;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import com.apap.director.im.util.SimpleBinder;

public class StompServiceConnection implements ServiceConnection {

    StompService stompService;

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        Log.v("HAI/ServiceConnection", "Connected");
        SimpleBinder binder = (SimpleBinder) service;
        stompService = (StompService) binder.getService();
//                    ((App) getApplication()).getChatComponent().inject(chatService);



//        username = String.valueOf(usernameField.getText());
//        password = String.valueOf(passwordField.getText());
//        server = String.valueOf(serverField.getText());
//        port = 5222;

        stompService.connect();


        Log.v("HAI/LoginActivity", "WAITED");


    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        Log.v("HAIServiceConnection", "Disonnected");
    }

}
