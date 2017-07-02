package com.apap.director.client.data.net.service;

import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import com.apap.director.client.data.net.rest.Paths;
import com.apap.director.client.data.net.rest.service.KeyService;
import com.apap.director.client.data.net.to.MessageTO;
import com.apap.director.client.data.net.to.OneTimeKeyTO;
import com.apap.director.client.data.net.to.SignedKeyTO;
import com.apap.director.client.data.store.IdentityKeyStoreImpl;
import com.apap.director.client.data.store.PreKeyStoreImpl;
import com.apap.director.client.data.store.SessionStoreImpl;
import com.apap.director.client.data.store.SignedPreKeyStoreImpl;
import com.apap.director.client.domain.model.ContactKey;
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
import org.whispersystems.libsignal.state.SessionRecord;

import java.io.IOException;
import java.util.Collections;
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
    private static MessageAction messageAction;
    private static SessionStoreImpl sessionStore;
    private static IdentityKeyStoreImpl identityKeyStore;
    private static PreKeyStoreImpl preKeyStore;
    private static SignedPreKeyStoreImpl signedPreKeyStore;
    private static KeyService keyService;
    private static String TAG = "ClientService";
    private static String MESSAGE = "MESSAGE";

    private ClientService() {
        // not called
    }

    public static void init(MessageAction messageAction, SessionStoreImpl sessionStore, IdentityKeyStoreImpl identityKeyStore, PreKeyStoreImpl preKeyStore, SignedPreKeyStoreImpl signedPreKeyStore, KeyService keyService) {
        ClientService.messageAction = messageAction;
        ClientService.sessionStore = sessionStore;
        ClientService.identityKeyStore = identityKeyStore;
        ClientService.preKeyStore = preKeyStore;
        ClientService.keyService = keyService;
    }


    public static void connect(String cookie) {
        Log.v(TAG, "Cookie : " + cookie);
        HashMap<String, String> connectionHeaders = new HashMap<>();
        connectionHeaders.put("Cookie", cookie);

        client = Stomp.over(WebSocket.class, "ws://" + Paths.SERVER_IP + ":" + Paths.SERVER_PORT + Paths.WEBSOCKET_ENDPOINT + "/websocket", connectionHeaders);

        Log.v("HAI", "Connecting to websocket with cookie " + cookie + "...");
        StompHeader cookieHeader = new StompHeader("Cookie", cookie);
        client.connect();
        client.topic("/user/exchange/amq.direct/messages", Collections.singletonList(cookieHeader)).subscribe(messageAction, new ErrorAction());
        // client.lifecycle().subscribe(listener);
    }

    public static void sendTestMessage(final String to, final String from, final String text) {
        try {
            String address = "/app/message/test/" + to;
            Log.v(TAG, "Sending framed message! " + address);

            MessageTO frame = new MessageTO(from, text, 0);
            ObjectMapper mapper = new ObjectMapper();

            String json = mapper.writeValueAsString(frame);

            client.send("/app/message/test/" + to, json)
                    .subscribe(new Action1<Void>() {
                        @Override
                        public void call(Void aVoid) {
                            Log.v(MESSAGE, "Call onNext " + text);

                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            Log.v(MESSAGE, "Error " + text);

                        }
                    });

        } catch (JsonProcessingException e) {
            Log.getStackTraceString(e);
        }
    }

    public static void sendEncryptedMessage(final String to, final String from, final String text) {
        Realm realm = Realm.getDefaultInstance();

        try {
            String address = "/app/message/" + to;
            Log.v(TAG, "Sending framed message! " + address);

            ContactKey contactKey = realm.where(ContactKey.class).equalTo("keyBase64", to).findFirst();

            SignalProtocolAddress signalProtocolAddress = new SignalProtocolAddress(to, contactKey.getDeviceId());

            // Instantiate a SessionBuilder for a remote recipientId + deviceId tuple.
            SessionBuilder sessionBuilder = new SessionBuilder(sessionStore, preKeyStore, signedPreKeyStore,
                    identityKeyStore, signalProtocolAddress);

            // Build a session with a PreKey retrieved from the server.
            AsyncTask<Void, Void, OneTimeKeyTO> getKeyTask = new AsyncTask<Void, Void, OneTimeKeyTO>() {
                @Override
                protected OneTimeKeyTO doInBackground(Void... params) {
                    try {
                        return getOneTimeKey(to);
                    } catch (IOException | InvalidKeyException e) {
                        Log.getStackTraceString(e);
                        return null;
                    }
                }
            };

            OneTimeKeyTO oneTimeKeyTO = getKeyTask.execute().get();
            byte[] decoded = Base64.decode(oneTimeKeyTO.getKeyBase64(), Base64.NO_WRAP | Base64.URL_SAFE);

            ECPublicKey oneTimeKeyEC = Curve.decodePoint(decoded, 0);


            AsyncTask<Void, Void, SignedKeyTO> getSignedKeyTask = new AsyncTask<Void, Void, SignedKeyTO>() {
                @Override
                protected SignedKeyTO doInBackground(Void... params) {
                try {
                    return getSignedKey(to);
                } catch (IOException | InvalidKeyException e) {
                    Log.getStackTraceString(e);

                    return null;
                }
                }
            };

            SignedKeyTO signedKeyTO = getSignedKeyTask.execute().get();
            byte[] decodedKey = Base64.decode(signedKeyTO.getKeyBase64(), Base64.NO_WRAP | Base64.URL_SAFE);
            byte[] decodedSignature = Base64.decode(signedKeyTO.getSignatureBase64(), Base64.NO_WRAP | Base64.URL_SAFE);


            ECPublicKey signedKeyEC = Curve.decodePoint(decodedKey, 0);
            IdentityKey contactIdentity = new IdentityKey(contactKey.getSerialized(), 0);
            SessionRecord mySession = sessionStore.loadSession(signalProtocolAddress);

            if (mySession == null) {
                sessionStore.storeSession(signalProtocolAddress, new SessionRecord());

                PreKeyBundle preKeyBundle = new PreKeyBundle(0, 0, oneTimeKeyTO.getOneTimeKeyId(), oneTimeKeyEC, signedKeyTO.getSignedKeyId(), signedKeyEC, decodedSignature, contactIdentity);
                sessionBuilder.process(preKeyBundle);
                realm.close();

            }

            SessionCipher sessionCipher = new SessionCipher(sessionStore, preKeyStore, signedPreKeyStore, identityKeyStore, signalProtocolAddress);
            CiphertextMessage message = sessionCipher.encrypt(text.getBytes("UTF-8"));
            String encodedText = Base64.encodeToString(message.serialize(), Base64.URL_SAFE | Base64.NO_WRAP);

            MessageTO frame = new MessageTO(from, encodedText, message.getType());
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(frame);

            client.send("/app/message/test/" + to, json)
                    .subscribe(new Action1<Void>() {
                        @Override
                        public void call(Void aVoid) {
                            Log.v(MESSAGE, "Call onNext " + text);

                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            Log.v(MESSAGE, "Error " + text);

                        }
                    });

            realm.close();

        } catch (UntrustedIdentityException | InvalidKeyException | IOException | InterruptedException | ExecutionException e) {
            Log.getStackTraceString(e);
        }
        finally {
            realm.close();

        }
    }

    private static OneTimeKeyTO getOneTimeKey(String to) throws IOException, InvalidKeyException {
        Call<OneTimeKeyTO> getKeyCall = keyService.getOneTimeKey(to);
        Response<OneTimeKeyTO> response = getKeyCall.execute();

        return response.body();
    }

    private static SignedKeyTO getSignedKey(String to) throws IOException, InvalidKeyException {

        Call<SignedKeyTO> getKeyCall = keyService.getSignedKey(to);
        Response<SignedKeyTO> response = getKeyCall.execute();

        return response.body();
    }

    public static void sendMessage(final String text) {
        Log.v("HAI/StompService", "Sending default message!");
        client.send("/app/hello", text)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Log.v(MESSAGE, "Call onNext " + text);

                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Log.v(MESSAGE, "Error " + text);

                    }
                });
    }

    public static void disconnect() {
        client.disconnect();
    }
}
