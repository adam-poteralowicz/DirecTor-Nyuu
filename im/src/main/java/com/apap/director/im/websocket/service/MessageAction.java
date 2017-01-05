package com.apap.director.im.websocket.service;

import android.util.Base64;
import android.util.Log;

import com.apap.director.db.manager.DatabaseManager;
import com.apap.director.db.realm.model.Account;
import com.apap.director.db.realm.model.Contact;
import com.apap.director.db.realm.model.ContactKey;
import com.apap.director.db.realm.model.Message;
import com.apap.director.db.realm.to.MessageTO;
import com.apap.director.im.signal.DirectorIdentityKeyStore;
import com.apap.director.im.signal.DirectorPreKeyStore;
import com.apap.director.im.signal.DirectorSessionStore;
import com.apap.director.im.signal.DirectorSignedPreKeyStore;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.whispersystems.libsignal.SessionBuilder;
import org.whispersystems.libsignal.SessionCipher;
import org.whispersystems.libsignal.SignalProtocolAddress;
import org.whispersystems.libsignal.protocol.CiphertextMessage;

import java.io.IOException;

import javax.inject.Inject;

import io.realm.Realm;
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

        Realm realm = Realm.getDefaultInstance();
        //TODO: decode message and add it to database;
        Log.v("HAI/MessageAction", stompMessage.getPayload());

        try {
            ObjectMapper mapper = new ObjectMapper();
            MessageTO messageTO = mapper.readValue(stompMessage.getPayload(), MessageTO.class);

            realm.beginTransaction();
            ContactKey key = realm.where(ContactKey.class).equalTo("name", messageTO.getFrom()).findFirst();
            Contact contact = realm.where(Contact.class).equalTo("name", messageTO.getFrom()).findFirst();

            long lastId = realm.where(Message.class).max("id").longValue();

            Message newMessage = realm.createObject(Message.class);
            newMessage.setId(lastId+1);
            newMessage.setContent(messageTO.getMessage());

            Account owner = realm.where(Account.class).equalTo("active", true).findFirst();
            newMessage.setAccount(owner);


        } catch (IOException e) {
            Log.v("HAI/MessageAction", "Incorrect message frame");
        }


    }
}
