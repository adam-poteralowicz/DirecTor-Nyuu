package com.apap.director.im.websocket.service;

import android.util.Base64;
import android.util.Log;

import com.apap.director.db.realm.model.Account;
import com.apap.director.db.realm.model.Contact;
import com.apap.director.db.realm.model.ContactKey;
import com.apap.director.db.realm.model.Conversation;
import com.apap.director.db.realm.model.Message;
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

import org.whispersystems.libsignal.DuplicateMessageException;
import org.whispersystems.libsignal.IdentityKeyPair;
import org.whispersystems.libsignal.InvalidKeyException;
import org.whispersystems.libsignal.InvalidKeyIdException;
import org.whispersystems.libsignal.InvalidMessageException;
import org.whispersystems.libsignal.InvalidVersionException;
import org.whispersystems.libsignal.LegacyMessageException;
import org.whispersystems.libsignal.NoSessionException;
import org.whispersystems.libsignal.SessionCipher;
import org.whispersystems.libsignal.SignalProtocolAddress;
import org.whispersystems.libsignal.UntrustedIdentityException;
import org.whispersystems.libsignal.logging.SignalProtocolLogger;
import org.whispersystems.libsignal.protocol.CiphertextMessage;
import org.whispersystems.libsignal.protocol.PreKeySignalMessage;
import org.whispersystems.libsignal.protocol.SignalMessage;
import org.whispersystems.libsignal.state.SessionRecord;
import org.whispersystems.libsignal.state.SessionState;

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

    @Inject
    public MessageAction(DirectorPreKeyStore preKeyStore, DirectorIdentityKeyStore identityKeyStore, DirectorSessionStore sessionStore, DirectorSignedPreKeyStore signedPreKeyStore, MessageManager messageManager, ContactManager contactManager, ConversationManager conversationManager) {
        this.preKeyStore = preKeyStore;
        this.identityKeyStore = identityKeyStore;
        this.sessionStore = sessionStore;
        this.signedPreKeyStore = signedPreKeyStore;
        this.messageManager = messageManager;
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

            ContactKey key = localRealm.where(ContactKey.class)
                    .equalTo("keyBase64", frame.getFrom())
                    .equalTo("account.active", true)
                    .findFirst();
            Contact contact = key.getContact();

            Log.v("HAI/MessageAction", "Contact name "+contact.getName());

            Conversation conversation = localRealm.where(Conversation.class).equalTo("contact.id", contact.getId()).findFirst();

            SignalProtocolAddress address = new SignalProtocolAddress(frame.getFrom(), 0);
            SessionCipher cipher = new SessionCipher(sessionStore, preKeyStore, signedPreKeyStore, identityKeyStore, address);
            byte[] decodedMessage = Base64.decode(frame.getMessage(), Base64.NO_WRAP | Base64.URL_SAFE);
            String finalMessage;
            SessionRecord mySession = sessionStore.loadSession(address);
            SessionState sessionState = mySession.getSessionState();

            if(mySession == null){
                Log.v("HAI/MessageAction", "Session is null");
                sessionStore.storeSession(address, new SessionRecord());
            }


            if(frame.getType()== CiphertextMessage.PREKEY_TYPE){

                    Log.v("HAI/MessageAction", "Unack");
                    finalMessage = new String(cipher.decrypt(new PreKeySignalMessage(decodedMessage)));
            }
            else{
                Log.v("HAI/MessageAction", "Acked");
                finalMessage = new String(cipher.decrypt(new SignalMessage(decodedMessage)));
            }

            Log.v("HAI/MessageAction", "adding message "+frame.getMessage());
            messageManager.addMessage(conversation, finalMessage, frame.getFrom(), false);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (DuplicateMessageException e) {
            e.printStackTrace();
        } catch (InvalidMessageException e) {
            Log.v("HAI/MessageAction","Invalid message");


            e.printStackTrace();
        } catch (UntrustedIdentityException e) {
            e.printStackTrace();
        } catch (InvalidKeyIdException e) {
            e.printStackTrace();
        } catch (InvalidVersionException e) {
            e.printStackTrace();
        } catch (LegacyMessageException e) {
            e.printStackTrace();
        } catch (NoSessionException e) {
            e.printStackTrace();
        }
    }
}
