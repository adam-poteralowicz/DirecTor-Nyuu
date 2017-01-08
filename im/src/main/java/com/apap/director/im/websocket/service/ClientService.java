package com.apap.director.im.websocket.service;

import android.util.Log;

import com.apap.director.db.realm.model.ContactKey;
import com.apap.director.db.realm.to.MessageTO;
import com.apap.director.im.signal.DirectorIdentityKeyStore;
import com.apap.director.im.signal.DirectorPreKeyStore;
import com.apap.director.im.signal.DirectorSessionStore;
import com.apap.director.im.signal.DirectorSignedPreKeyStore;
import com.apap.director.network.rest.Paths;
import com.apap.director.network.rest.service.KeyService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.java_websocket.WebSocket;
import org.whispersystems.libsignal.SessionBuilder;
import org.whispersystems.libsignal.SessionCipher;
import org.whispersystems.libsignal.SignalProtocolAddress;
import org.whispersystems.libsignal.protocol.CiphertextMessage;

import java.util.Arrays;
import java.util.HashMap;

import io.realm.Realm;
import rx.functions.Action1;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompHeader;
import ua.naiksoftware.stomp.client.StompClient;

/**
 * Created by Ala on 07/01/2017.
 */

public class ClientService {

    public static StompClient client;
    public static MessageAction messageAction;
    private static DirectorSessionStore sessionStore;
    private static DirectorIdentityKeyStore identityKeyStore;
    private static DirectorPreKeyStore preKeyStore;
    private static DirectorSignedPreKeyStore signedPreKeyStore;
    private static KeyService keyService;


    public static void init(MessageAction messageAction, DirectorSessionStore sessionStore, DirectorIdentityKeyStore identityKeyStore, DirectorPreKeyStore preKeyStore, DirectorSignedPreKeyStore signedPreKeyStore, KeyService keyService){
        ClientService.messageAction = messageAction;
        ClientService.sessionStore = sessionStore;
        ClientService.identityKeyStore = identityKeyStore;
        ClientService.preKeyStore = preKeyStore;
        ClientService.keyService = keyService;

    }


    public static void connect(String cookie){
        Log.v("HAI/StompService", "Cookie : "+cookie);
        HashMap<String, String> connectionHeaders = new HashMap<>();
        connectionHeaders.put("Cookie", cookie);


        client = Stomp.over(WebSocket.class, "ws://"+ Paths.SERVER_IP+":"+Paths.SERVER_PORT + Paths.WEBSOCKET_ENDPOINT+"/websocket", connectionHeaders);

        Log.v("HAI", "Connecting to websocket with cookie "+ cookie+"...");
        StompHeader cookieHeader = new StompHeader("Cookie", cookie);
        client.connect();
        client.topic("/user/exchange/amq.direct/messages", Arrays.asList(cookieHeader)).subscribe(messageAction, new ErrorAction());
        // client.lifecycle().subscribe(listener);
    }

    public static void sendTestMessage(final String to, final String from, final String text){
        try {
            String address = "/app/message/test/"+to;
            Log.v("HAI/StompService", "Sending framed message! " +address);

            MessageTO frame = new MessageTO(from, text);
            ObjectMapper mapper = new ObjectMapper();

            String json = mapper.writeValueAsString(frame);

            client.send("/app/message/test/"+to,json)
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


        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }

    public static void sendEncryptedMessage(final String to, final String from, final String text){
        try {
            String address = "/app/message/"+to;
            Log.v("HAI/StompService", "Sending framed message! " +address);

            MessageTO frame = new MessageTO(from, text);
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(frame);

            Realm realm = Realm.getDefaultInstance();
            ContactKey contactKey = realm.where(ContactKey.class).equalTo("keyBase64",to).findFirst();

            SignalProtocolAddress signalProtocolAddress = new SignalProtocolAddress(to, contactKey.getDeviceId());

            if(contactKey.getContact().getConversation().getSessions().size()==0){

                // Instantiate a SessionBuilder for a remote recipientId + deviceId tuple.
                SessionBuilder sessionBuilder = new SessionBuilder(sessionStore, preKeyStore, signedPreKeyStore,
                        identityKeyStore, signalProtocolAddress);

                // Build a session with a PreKey retrieved from the server.
              //  sessionBuilder.process(retrievedPreKey);

            }


//            SessionCipher     sessionCipher = new SessionCipher(sessionStore, recipientId, deviceId);
//            CiphertextMessage message      = sessionCipher.encrypt("Hello world!".getBytes("UTF-8"));
//            sessionCipher.
//            message.

            client.send("/app/message/test/"+to,json)
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


        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

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

    public static void disconnect(){
        client.disconnect();
    }

}
