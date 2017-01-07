package com.apap.director.im.websocket.service;

import android.util.Base64;
import android.util.Log;

import com.apap.director.db.manager.DatabaseManager;
import com.apap.director.db.realm.model.Account;
import com.apap.director.db.realm.model.Contact;
import com.apap.director.db.realm.model.ContactKey;
import com.apap.director.db.realm.model.Conversation;
import com.apap.director.db.realm.model.Message;
import com.apap.director.db.realm.model.Session;
import com.apap.director.db.realm.to.MessageTO;
import com.apap.director.im.signal.DirectorIdentityKeyStore;
import com.apap.director.im.signal.DirectorPreKeyStore;
import com.apap.director.im.signal.DirectorSessionStore;
import com.apap.director.im.signal.DirectorSignedPreKeyStore;
import com.apap.director.manager.ContactManager;
import com.apap.director.manager.ConversationManager;
import com.apap.director.manager.MessageManager;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.whispersystems.libsignal.IdentityKeyPair;
import org.whispersystems.libsignal.InvalidKeyException;
import org.whispersystems.libsignal.SessionBuilder;
import org.whispersystems.libsignal.SessionCipher;
import org.whispersystems.libsignal.SignalProtocolAddress;
import org.whispersystems.libsignal.protocol.CiphertextMessage;
import org.whispersystems.libsignal.state.PreKeyBundle;

import java.io.IOException;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmList;
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

            ContactKey key = localRealm.where(ContactKey.class).equalTo("keyBase64", frame.getFrom()).findFirst();
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

                conversation = conversationManager.addConversation(contact, session);
            }

            Log.v("HAI/MessageAction", "adding message "+frame.getMessage());
            messageManager.addMessage(conversation, frame.getMessage(), frame.getFrom(), false);


        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }

////        String payload = stompMessage.getPayload();
////
////        ObjectMapper mapper = new ObjectMapper();
////
////        SessionCipher sessionCipher = new SessionCipher(sessionStore, preKeyStore, signedPreKeyStore, identityKeyStore, address);
////        CiphertextMessage message      = sessionCipher.decrypt()
//
//        Realm realm = Realm.getDefaultInstance();
//        //TODO: decode message and add it to database;
//        Log.v("HAI/MessageAction", stompMessage.getPayload());
//
//        try {
//            ObjectMapper mapper = new ObjectMapper();
//            MessageTO messageTO = mapper.readValue(stompMessage.getPayload(), MessageTO.class);
//
//            Account owner = realm.where(Account.class).equalTo("active", true).findFirst();
//
//            realm.beginTransaction();
//            ContactKey key = realm.where(ContactKey.class).equalTo("keyBase64", messageTO.getFrom()).findFirst();
//            Contact contact = key.getContact();
//
//            if(contact == null){
//                Log.v("HAI/MessageAction", "No such contact key: "+messageTO.getFrom());
//                if(messageTO.getFrom().equals(owner.getKeyBase64())){
//
//                    //if it is a message from the current account, create new contact ("Me");
//                        Contact me = realm.createObject(Contact.class);
//                        me.setAccount(owner);
//                        me.setName("Me");
//                        me.setId(realm.where(Contact.class).max("id").longValue());
//
//                        ContactKey myKey = realm.createObject(ContactKey.class);
//                        myKey.setKeyBase64(owner.getKeyBase64());
//                        IdentityKeyPair myKeyPair = new IdentityKeyPair(owner.getKeyPair());
//                        myKey.setSerialized(myKeyPair.getPublicKey().serialize());
//                        myKey.setDeviceId(0);
//                        myKey.setId(realm.where(ContactKey.class).max("id").longValue());
//                        myKey.setAccount(owner);
//                        myKey.setContact(me);
//
//                        RealmList<ContactKey> myKeys = new RealmList<>(myKey);
//
//                        me.setContactKeys(myKeys);
//
//                        contact = me;
//                }
//                else{
//                    realm.commitTransaction();
//                    return;
//                }
//
//            }
//
//            Conversation existingConversation = realm.where(Conversation.class).equalTo("contact.name", contact.getName()).findFirst();
//            if(existingConversation == null){
//                Log.v("HAI/MessageAction", "No such conversation");
//                existingConversation = realm.createObject(Conversation.class);
//                existingConversation.setAccount(owner);
//                existingConversation.setContact(contact);
//                existingConversation.setId(realm.where(Conversation.class).max("id").longValue()+1);
//                contact.setConversation(existingConversation);
//            }
//
////            long lastId = realm.where(Message.class).max("id").longValue();
////
////            Message newMessage = realm.createObject(Message.class);
////            newMessage.setId(lastId+1);
////            newMessage.setContent(messageTO.getMessage());
////            newMessage.setConversation(existingConversation);
////            newMessage.setAccount(owner);
////            newMessage.setMine(false);
////            realm.commitTransaction();
//
//            realm.commitTransaction();
//            Log.v("HAI/MessageAction", "Adding msg");
//            messageManager.addMessage(existingConversation, stompMessage.getPayload(), "me", false);
//
//
//        } catch (IOException e) {
//            realm.commitTransaction();
//            Log.e("HAI/MessageAction", "Incorrect message frame", e);
//
//        } catch (InvalidKeyException e) {
//            realm.commitTransaction();
//            e.printStackTrace();
//        }


    }
}
