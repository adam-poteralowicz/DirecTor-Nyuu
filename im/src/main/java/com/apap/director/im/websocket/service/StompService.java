package com.apap.director.im.websocket.service;

import android.util.Log;

import com.apap.director.im.config.IMConfig;
import com.apap.director.im.signal.DirectorIdentityKeyStore;
import com.apap.director.im.signal.DirectorPreKeyStore;
import com.apap.director.im.signal.DirectorSessionStore;
import com.apap.director.im.signal.DirectorSignedPreKeyStore;

import org.java_websocket.WebSocket;

import javax.inject.Inject;

import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.client.StompClient;
import ua.naiksoftware.stomp.client.StompMessage;

public class StompService {

    private StompClient client;

    MessageAction messageAction;

    @Inject
    public StompService(MessageAction messageAction) {
        this.messageAction = messageAction;
        client = Stomp.over(WebSocket.class, "ws://"+ IMConfig.SERVER_IP+":7500" + IMConfig.WEBSOCKET_ENDPOINT+"/websocket");

    }

    public void connect(){
        Log.v("HAI", "Connecting to websocket...");
        client.connect();
        Log.v("HAI", "connected: " + client.isConnected());
        client.topic("/topic/greetings").subscribe(messageAction, new ErrorAction());
        client.send("/app/hello", "hai");

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
