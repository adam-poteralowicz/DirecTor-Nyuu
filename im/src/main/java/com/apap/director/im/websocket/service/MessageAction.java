package com.apap.director.im.websocket.service;

import android.util.Base64;
import android.util.Log;

import com.apap.director.db.realm.model.Account;
import com.apap.director.db.realm.model.Contact;
import com.apap.director.db.realm.model.ContactKey;
import com.apap.director.db.realm.model.Conversation;
import com.apap.director.db.realm.model.Session;
import com.apap.director.db.realm.to.MessageTO;
import com.apap.director.signal.DirectorIdentityKeyStore;
import com.apap.director.signal.DirectorPreKeyStore;
import com.apap.director.signal.DirectorSessionStore;
import com.apap.director.signal.DirectorSignedPreKeyStore;
import com.apap.director.manager.ContactManager;
import com.apap.director.manager.ConversationManager;
import com.apap.director.manager.MessageManager;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.whispersystems.libsignal.IdentityKeyPair;
import org.whispersystems.libsignal.InvalidKeyException;

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
    private MessageManager messageManager;
    private ContactManager contactManager;
    private ConversationManager conversationManager;

    @Inject
    public MessageAction(DirectorPreKeyStore preKeyStore, DirectorIdentityKeyStore identityKeyStore, DirectorSessionStore sessionStore, DirectorSignedPreKeyStore signedPreKeyStore, MessageManager messageManager, ContactManager contactManager, ConversationManager conversationManager) {
        this.preKeyStore = preKeyStore;
        this.identityKeyStore = identityKeyStore;
        this.sessionStore = sessionStore;
        this.signedPreKeyStore = signedPreKeyStore;
        this.messageManager = messageManager;
        this.contactManager = contactManager;
        this.conversationManager = conversationManager;
    }

    @Override
    public void call(StompMessage stompMessage) {
        try {
            Log.v("HAI/MessageAction", "PAYLOAD: "+stompMessage.getPayload());

            ObjectMapper mapper = new ObjectMapper();
            MessageTO frame = mapper.readValue(stompMessage.getPayload(), MessageTO.class);
            Log.v("HAI/MessageAction", "POINTCUT: FRAME");


            Log.v("HAI/MessageAction", "POINTCUT: GET CONTACT");
            Realm localRealm = Realm.getDefaultInstance();
            Account active = localRealm.where(Account.class).equalTo("active", true).findFirst();
            String keyBase64 = Base64.encodeToString(new IdentityKeyPair(active.getKeyPair()).getPublicKey().serialize(), Base64.NO_WRAP | Base64.URL_SAFE);

            ContactKey key = localRealm.where(ContactKey.class)
                    .equalTo("keyBase64", frame.getFrom())
                    .equalTo("account.active", true)
                    .findFirst();
            Contact contact = key.getContact();

            Log.v("HAI/MessageAction", "POINTCUT: GET ACTIVE");

            if(contact == null){

                if(frame.getFrom().equals(active.getKeyBase64())){
                    Log.v("HAI/MessageAction", "POINTCUT BEFORE ADD");

                    contactManager.addContact("me", keyBase64);

                    localRealm.beginTransaction();
                        key = localRealm.where(ContactKey.class).equalTo("keyBase64", frame.getFrom()).findFirst();
                        contact = key.getContact();
                    localRealm.commitTransaction();

                    Log.v("HAI/MessageAction", "POINTCUT AFTER ADD");

                }

            }

            Log.v("HAI/MessageAction", "Contact name "+contact.getName());

            Conversation conversation = localRealm.where(Conversation.class).equalTo("contact.id", contact.getId()).findFirst();

            if(conversation == null){
                Session session = new Session();
                session.setId(localRealm.where(Session.class).max("id").longValue()+1);
                session.setDeviceId(0);
                session.setAccount(active);
                session.setName(key.getKeyBase64());
                session.setSerializedKey(key.getSerialized());

                conversation = conversationManager.addConversation(contact);
            }

            Log.v("HAI/MessageAction", "adding message "+frame.getMessage());
            messageManager.addMessage(conversation, frame.getMessage(), frame.getFrom(), false);


        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }



    }
}
