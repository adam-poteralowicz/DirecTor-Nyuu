package com.apap.director.im.websocket.service;

import android.util.Log;

import com.apap.director.im.config.IMConfig;

import org.java_websocket.WebSocket;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

import javax.inject.Inject;

import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompHeader;
import ua.naiksoftware.stomp.client.StompClient;
import ua.naiksoftware.stomp.client.StompMessage;

public class StompService {

    private StompClient client;

    MessageAction messageAction;


    @Inject
    public StompService(MessageAction messageAction) {
        this.messageAction = messageAction;
    }

    public void connect(String cookie, String username){

        Log.v("HAI/StompService", "Cookie : "+cookie);
        HashMap<String, String> connectionHeaders = new HashMap<>();
        connectionHeaders.put("Cookie", cookie);
        connectionHeaders.put(cookie.split("=")[0], cookie.split("=")[1]);
        connectionHeaders.put("simpUser", username);

        client = Stomp.over(WebSocket.class, "ws://"+ IMConfig.SERVER_IP+":7500" + IMConfig.WEBSOCKET_ENDPOINT+"/websocket", connectionHeaders);

        Log.v("HAI", "Connecting to websocket with cookie "+ cookie+"...");
     //   Collections.singletonList(new StompHeader("user", ""));
        StompHeader userHeader = new StompHeader("simpUser", username);
        StompHeader cookieHeader = new StompHeader("Cookie", cookie);
        StompHeader destinationHeader = new StompHeader("destination", "/app/hello");
        client.connect(Arrays.asList(userHeader, cookieHeader));

        Log.v("HAI", "connected: " + client.isConnected());
        Log.v("HAI", "connected: " + client.isConnected());
        Log.v("HAI", "connected: " + client.isConnected());
        Log.v("HAI", "connected: " + client.isConnected());
        Log.v("HAI", "connected: " + client.isConnected());
        Log.v("HAI", "connected: " + client.isConnected());
        //client.topic("/topic/greetings").subscribe(messageAction, new ErrorAction());

        client.topic("/user/exchange/amq.direct/messages", Arrays.asList(userHeader, cookieHeader)).subscribe(messageAction, new ErrorAction());
        client.send(new StompMessage("SEND", Arrays.asList(userHeader, destinationHeader, cookieHeader), "hai"));

    }

    public void disconnect(){
        client.disconnect();
    }

    public void sendMessage(String keyBase64, String text){
        // TODO: Encode the message and send via stomp client
    }

    public void logIn(){



    }

    public void signUp(){

    }



}
