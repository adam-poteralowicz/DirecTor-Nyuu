package com.apap.director.im.websocket.service;

import com.apap.director.im.config.IMConfig;
import com.apap.director.im.signal.DirectorIdentityKeyStore;
import com.apap.director.im.signal.DirectorPreKeyStore;
import com.apap.director.im.signal.DirectorSessionStore;
import com.apap.director.im.signal.DirectorSignedPreKeyStore;

import org.java_websocket.WebSocket;

import javax.inject.Inject;

import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.client.StompClient;

public class StompService {

    private StompClient client;

    @Inject
    public DirectorPreKeyStore preKeyStore;

    @Inject
    public DirectorSessionStore sessionStore;

    @Inject
    public DirectorIdentityKeyStore identityKeyStore;

    @Inject
    public DirectorSignedPreKeyStore signedPreKeyStore;

    public StompService() {
        client = Stomp.over(WebSocket.class, "ws://"+ IMConfig.SERVER_IP + IMConfig.WEBSOCKET_ENDPOINT+ "/websocket");
    }

    public void connect(){
        client.connect();
        //client.topic("/user/queue").subscribe(new MessageAction(), new ErrorAction());

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
