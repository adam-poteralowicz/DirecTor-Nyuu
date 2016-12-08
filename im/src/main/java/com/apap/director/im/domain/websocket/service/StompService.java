package com.apap.director.im.domain.websocket.service;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.apap.director.im.config.IMConfig;
import com.apap.director.im.util.SimpleBinder;

import org.java_websocket.WebSocket;

import rx.Subscription;
import rx.functions.Action1;
import rx.plugins.RxJavaHooks;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.client.StompClient;
import ua.naiksoftware.stomp.client.StompMessage;

/**
 * Created by Ala on 06/12/2016.
 */

public class StompService extends Service {

    private StompClient client;

    public StompService() {
        client = Stomp.over(WebSocket.class, "ws://"+ IMConfig.SERVER_IP + IMConfig.WEBSOCKET_ENDPOINT+ "/websocket");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new SimpleBinder(this);
    }

    public void connect(){

        client.connect();
        Subscription sub = client.topic("/topic/greetings").subscribe(new Action1<StompMessage>(){

            @Override
            public void call(StompMessage stompMessage) {
                Log.v("HAI", "Message "+stompMessage.getPayload());
            }
        });

        client.send("/hello", "My first STOMP message!").subscribe();


    }

    public void disconnect(){
        client.disconnect();
    }

}
