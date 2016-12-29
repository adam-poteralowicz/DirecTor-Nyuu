package com.apap.director.im.websocket.service;

import android.util.Base64;
import android.util.Log;

import com.apap.director.db.manager.DatabaseManager;
import com.apap.director.im.signal.DirectorIdentityKeyStore;
import com.apap.director.im.signal.DirectorPreKeyStore;
import com.apap.director.im.signal.DirectorSessionStore;
import com.apap.director.im.signal.DirectorSignedPreKeyStore;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.whispersystems.libsignal.SessionBuilder;
import org.whispersystems.libsignal.SessionCipher;
import org.whispersystems.libsignal.SignalProtocolAddress;
import org.whispersystems.libsignal.protocol.CiphertextMessage;

import javax.inject.Inject;

import rx.functions.Action1;
import ua.naiksoftware.stomp.client.StompMessage;

public class MessageAction implements Action1<StompMessage> {

    private DirectorPreKeyStore preKeyStore;
    private DirectorIdentityKeyStore identityKeyStore;
    private DirectorSessionStore sessionStore;
    private DirectorSignedPreKeyStore signedPreKeyStore;

    @Inject
    public MessageAction(DirectorPreKeyStore preKeyStore, DirectorIdentityKeyStore identityKeyStore, DirectorSessionStore sessionStore, DirectorSignedPreKeyStore signedPreKeyStore) {
        this.preKeyStore = preKeyStore;
        this.identityKeyStore = identityKeyStore;
        this.sessionStore = sessionStore;
        this.signedPreKeyStore = signedPreKeyStore;
    }

    @Override
    public void call(StompMessage stompMessage) {

//        String payload = stompMessage.getPayload();
//
//        ObjectMapper mapper = new ObjectMapper();
//
//        SessionCipher sessionCipher = new SessionCipher(sessionStore, preKeyStore, signedPreKeyStore, identityKeyStore, address);
//        CiphertextMessage message      = sessionCipher.decrypt()


        //TODO: decode message and add it to database;
        Log.v("HAI/MessageAction", stompMessage.getPayload());

    }
}
