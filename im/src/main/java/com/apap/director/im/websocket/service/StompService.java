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
    private String cookie;

    MessageAction messageAction;

    @Inject
    public StompService(MessageAction messageAction) {
        this.messageAction = messageAction;
    }

    public void connect(String cookie){
        this.cookie = cookie;
        Log.v("HAI/StompService", "Cookie : "+cookie);
        HashMap<String, String> connectionHeaders = new HashMap<>();
        connectionHeaders.put("Cookie", cookie);

        client = Stomp.over(WebSocket.class, "ws://"+ IMConfig.SERVER_IP+":7500" + IMConfig.WEBSOCKET_ENDPOINT+"/websocket", connectionHeaders);

        Log.v("HAI", "Connecting to websocket with cookie "+ cookie+"...");
        StompHeader cookieHeader = new StompHeader("Cookie", cookie);
        client.connect(Arrays.asList(cookieHeader));

        client.topic("/user/exchange/amq.direct/messages", Arrays.asList(cookieHeader)).subscribe(messageAction, new ErrorAction());
    }

    public void disconnect(){
        client.disconnect();
    }

    public void sendMessage(String recipientKeyBase64, String text){
        // TODO: Encode the message and send via stomp client

        StompHeader cookieHeader = new StompHeader("Cookie", cookie);
        StompHeader destinationHeader = new StompHeader("destination", "/app/message/"+recipientKeyBase64);
        client.send(new StompMessage("SEND", Arrays.asList(destinationHeader, cookieHeader), "hai"));

    }



}
