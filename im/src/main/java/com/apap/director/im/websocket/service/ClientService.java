package com.apap.director.im.websocket.service;

import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.util.Pair;

import com.apap.director.db.realm.model.ContactKey;
import com.apap.director.db.realm.model.Session;
import com.apap.director.db.realm.to.MessageTO;
import com.apap.director.db.realm.to.OneTimeKeyTO;
import com.apap.director.db.realm.to.SignedKeyTO;
import com.apap.director.signal.DirectorIdentityKeyStore;
import com.apap.director.signal.DirectorPreKeyStore;
import com.apap.director.signal.DirectorSessionStore;
import com.apap.director.signal.DirectorSignedPreKeyStore;
import com.apap.director.network.rest.Paths;
import com.apap.director.network.rest.service.KeyService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.java_websocket.WebSocket;
import org.whispersystems.libsignal.IdentityKey;
import org.whispersystems.libsignal.InvalidKeyException;
import org.whispersystems.libsignal.SessionBuilder;
import org.whispersystems.libsignal.SessionCipher;
import org.whispersystems.libsignal.SignalProtocolAddress;
import org.whispersystems.libsignal.UntrustedIdentityException;
import org.whispersystems.libsignal.ecc.Curve;
import org.whispersystems.libsignal.ecc.ECPublicKey;
import org.whispersystems.libsignal.protocol.CiphertextMessage;
import org.whispersystems.libsignal.state.PreKeyBundle;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Response;
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

            Realm realm = Realm.getDefaultInstance();
            ContactKey contactKey = realm.where(ContactKey.class).equalTo("keyBase64",to).findFirst();

            SignalProtocolAddress signalProtocolAddress = new SignalProtocolAddress(to, contactKey.getDeviceId());

            Session session = realm.where(Session.class).equalTo("name", to).equalTo("deviceId", contactKey.getDeviceId()).findFirst();

            if(session == null){

                // Instantiate a SessionBuilder for a remote recipientId + deviceId tuple.
                SessionBuilder sessionBuilder = new SessionBuilder(sessionStore, preKeyStore, signedPreKeyStore,
                        identityKeyStore, signalProtocolAddress);

                // Build a session with a PreKey retrieved from the server.
                AsyncTask<Void, Void, ECPublicKey> getKeyTask = new AsyncTask<Void, Void, ECPublicKey>() {
                    @Override
                    protected ECPublicKey doInBackground(Void... params) {
                        try {
                            return getOneTimeKey(to);
                        } catch (IOException e) {
                            e.printStackTrace();
                            return null;
                        } catch (InvalidKeyException e) {
                            e.printStackTrace();
                            return null;
                        }
                    }
                };

                ECPublicKey oneTimeKey = getKeyTask.execute().get();

                AsyncTask<Void, Void, Pair<ECPublicKey, byte[]>> getSignedKeyTask = new AsyncTask<Void, Void, Pair<ECPublicKey, byte[]>>() {
                    @Override
                    protected Pair<ECPublicKey, byte[]> doInBackground(Void... params) {
                        try {
                            return getSignedKey(to);
                        } catch (IOException e) {
                            e.printStackTrace();
                            return null;
                        } catch (InvalidKeyException e) {
                            e.printStackTrace();
                            return null;
                        }
                    }
                };

                Pair<ECPublicKey, byte[]> signedKey = getSignedKeyTask.execute().get();

                IdentityKey contactIdentity = new IdentityKey(contactKey.getSerialized(),0);

                PreKeyBundle preKeyBundle = new PreKeyBundle(0, contactKey.getDeviceId(), 0, oneTimeKey, 0, signedKey.first, signedKey.second, contactIdentity);
                sessionBuilder.process(preKeyBundle);

            }

            SessionCipher sessionCipher = new SessionCipher(sessionStore, preKeyStore, signedPreKeyStore, identityKeyStore, new SignalProtocolAddress(to, contactKey.getDeviceId()));
            CiphertextMessage message = sessionCipher.encrypt(text.getBytes("UTF-8"));
            String encodedText = Base64.encodeToString(message.serialize(), Base64.URL_SAFE | Base64.NO_WRAP);


            MessageTO frame = new MessageTO(from, encodedText);
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(frame);

            client.send("/app/message/test"+to,json)
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

            realm.close();

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (UntrustedIdentityException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    private static ECPublicKey getOneTimeKey(String to) throws IOException, InvalidKeyException {
        Call<OneTimeKeyTO> getKeyCall = keyService.getOneTimeKey(to);
        Response<OneTimeKeyTO> response = getKeyCall.execute();
        OneTimeKeyTO oneTimeKeyTO = response.body();

        byte[] decoded = Base64.decode(oneTimeKeyTO.getKeyBase64(), Base64.NO_WRAP | Base64.URL_SAFE);

        return Curve.decodePoint(decoded, 0);

    }

    private static Pair<ECPublicKey, byte[]> getSignedKey(String to) throws IOException, InvalidKeyException {

        Call<SignedKeyTO> getKeyCall = keyService.getSignedKey(to);
        Response<SignedKeyTO> response = getKeyCall.execute();
        SignedKeyTO signedKeyTO = response.body();

        byte[] decodedKey = Base64.decode(signedKeyTO.getKeyBase64(), Base64.NO_WRAP | Base64.URL_SAFE);
        byte[] decodedSignature = Base64.decode(signedKeyTO.getSignatureBase64(), Base64.NO_WRAP | Base64.URL_SAFE);

        return new Pair<ECPublicKey, byte[]>(Curve.decodePoint(decodedKey,0), decodedSignature);
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
