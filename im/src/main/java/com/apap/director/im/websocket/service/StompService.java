package com.apap.director.im.websocket.service;

import android.util.Log;

import com.apap.director.db.realm.to.MessageTO;
import com.apap.director.network.rest.Paths;

import org.java_websocket.WebSocket;

import java.util.Arrays;
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

        client = Stomp.over(WebSocket.class, "ws://"+ Paths.SERVER_IP+":"+Paths.SERVER_PORT + Paths.WEBSOCKET_ENDPOINT+"/websocket", connectionHeaders);

        Log.v("HAI", "Connecting to websocket with cookie "+ cookie+"...");
        StompHeader cookieHeader = new StompHeader("Cookie", cookie);
        client.connect(Arrays.asList(cookieHeader));

        client.topic("/user/exchange/amq.direct/messages", Arrays.asList(cookieHeader)).subscribe(messageAction, new ErrorAction());
    }

    public void disconnect(){
        client.disconnect();
    }

    public void sendMessage(String recipientKeyBase64, String text, String from){
        // TODO: Encode the message and send via stomp client

        StompHeader cookieHeader = new StompHeader("Cookie", cookie);
        StompHeader destinationHeader = new StompHeader("destination", "/app/message/test"+recipientKeyBase64);
        MessageTO frame = new MessageTO(from, text);

        client.send(new StompMessage("SEND", Arrays.asList(destinationHeader, cookieHeader), "hai"));

    }



}
