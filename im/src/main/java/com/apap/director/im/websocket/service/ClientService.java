package com.apap.director.im.websocket.service;

import android.util.Log;

import com.apap.director.network.rest.Paths;

import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;

import java.util.HashMap;

import rx.functions.Action0;
import rx.functions.Action1;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompHeader;
import ua.naiksoftware.stomp.client.StompClient;

/**
 * Created by Ala on 07/01/2017.
 */

public class ClientService {

    public static StompClient client;


    public static void connect(String cookie){
        Log.v("HAI/StompService", "Cookie : "+cookie);
        HashMap<String, String> connectionHeaders = new HashMap<>();
        connectionHeaders.put("Cookie", cookie);


        client = Stomp.over(WebSocket.class, "ws://"+ Paths.SERVER_IP+":"+Paths.SERVER_PORT + Paths.WEBSOCKET_ENDPOINT+"/websocket", connectionHeaders);

        Log.v("HAI", "Connecting to websocket with cookie "+ cookie+"...");
        StompHeader cookieHeader = new StompHeader("Cookie", cookie);
        client.connect();
        // client.topic("/user/exchange/amq.direct/messages", Arrays.asList(cookieHeader)).subscribe(messageAction, new ErrorAction());
        // client.lifecycle().subscribe(listener);
    }

    public static void sendMessage(final String text){
        Log.v("HAI/StompService", "Sending default message!");
        client.send("/app/hello",text)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Log.v("HAI/MESSAGE", "Call onNext " + text);

                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Log.v("HAI/MESSAGE", "Error " + text);

                    }
                });

    }

}
