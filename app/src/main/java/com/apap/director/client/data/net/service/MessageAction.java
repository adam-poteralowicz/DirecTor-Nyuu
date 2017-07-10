//package com.apap.director.client.data.net.service;
//
//import android.util.Base64;
//import android.util.Log;
//
//import com.apap.director.client.data.db.entity.ContactEntity;
//import com.apap.director.client.data.db.entity.ContactKeyEntity;
//import com.apap.director.client.data.db.entity.ConversationEntity;
//import com.apap.director.client.data.manager.MessageManager;
//import com.apap.director.client.data.net.to.MessageTO;
//import com.apap.director.client.data.store.IdentityKeyStoreImpl;
//import com.apap.director.client.data.store.PreKeyStoreImpl;
//import com.apap.director.client.data.store.SessionStoreImpl;
//import com.apap.director.client.data.store.SignedPreKeyStoreImpl;
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//import org.whispersystems.libsignal.DuplicateMessageException;
//import org.whispersystems.libsignal.InvalidKeyException;
//import org.whispersystems.libsignal.InvalidKeyIdException;
//import org.whispersystems.libsignal.InvalidMessageException;
//import org.whispersystems.libsignal.InvalidVersionException;
//import org.whispersystems.libsignal.LegacyMessageException;
//import org.whispersystems.libsignal.NoSessionException;
//import org.whispersystems.libsignal.SessionCipher;
//import org.whispersystems.libsignal.SignalProtocolAddress;
//import org.whispersystems.libsignal.UntrustedIdentityException;
//import org.whispersystems.libsignal.protocol.CiphertextMessage;
//import org.whispersystems.libsignal.protocol.PreKeySignalMessage;
//import org.whispersystems.libsignal.protocol.SignalMessage;
//import org.whispersystems.libsignal.state.SessionRecord;
//import org.whispersystems.libsignal.state.SessionState;
//
//import java.io.IOException;
//
//import javax.inject.Inject;
//
//import io.realm.Realm;
//import rx.functions.Action1;
//import ua.naiksoftware.stomp.client.StompMessage;
//
//public class MessageAction implements Action1<StompMessage> {
//
//    private PreKeyStoreImpl preKeyStore;
//    private IdentityKeyStoreImpl identityKeyStore;
//    private SessionStoreImpl sessionStore;
//    private SignedPreKeyStoreImpl signedPreKeyStore;
//    private MessageManager messageManager;
//    private String TAG = this.getClass().getSimpleName();
//
//    @Inject
//    public MessageAction(PreKeyStoreImpl preKeyStore, IdentityKeyStoreImpl identityKeyStore, SessionStoreImpl sessionStore, SignedPreKeyStoreImpl signedPreKeyStore, MessageManager messageManager) {
//        this.preKeyStore = preKeyStore;
//        this.identityKeyStore = identityKeyStore;
//        this.sessionStore = sessionStore;
//        this.signedPreKeyStore = signedPreKeyStore;
//        this.messageManager = messageManager;
//    }
//
//    @Override
//    public void call(StompMessage stompMessage) {
//        Realm localRealm = Realm.getDefaultInstance();
//
//        try {
//            Log.v(TAG, "PAYLOAD: " + stompMessage.getPayload());
//
//            ObjectMapper mapper = new ObjectMapper();
//            MessageTO frame = mapper.readValue(stompMessage.getPayload(), MessageTO.class);
//            Log.v(TAG, "POINTCUT: FRAME");
//
//
//            Log.v(TAG, "POINTCUT: GET CONTACT");
//
//            ContactKeyEntity key = localRealm.where(ContactKeyEntity.class)
//                    .equalTo("keyBase64", frame.getFrom())
//                    .equalTo("account.active", true)
//                    .findFirst();
//
//            localRealm.close();
//
//            ContactEntity contact = key.getContact();
//
//            Log.v(TAG, "ContactEntity name " + contact.getName());
//
//            ConversationEntity conversation = localRealm.where(ConversationEntity.class).equalTo("contacts.id", contact.getId()).findFirst();
//
//            SignalProtocolAddress address = new SignalProtocolAddress(frame.getFrom(), 0);
//            SessionCipher cipher = new SessionCipher(sessionStore, preKeyStore, signedPreKeyStore, identityKeyStore, address);
//            byte[] decodedMessage = Base64.decode(frame.getMessage(), Base64.NO_WRAP | Base64.URL_SAFE);
//            String finalMessage;
//            SessionRecord mySession = sessionStore.loadSession(address);
//            SessionState sessionState = mySession.getSessionState();
//
//            if (mySession == null) {
//                Log.v(TAG, "SessionEntity is null");
//                sessionStore.storeSession(address, new SessionRecord());
//            }
//
//
//            if (frame.getType() == CiphertextMessage.PREKEY_TYPE) {
//
//                Log.v(TAG, "Unack");
//                finalMessage = new String(cipher.decrypt(new PreKeySignalMessage(decodedMessage)));
//            } else {
//                Log.v(TAG, "Acked");
//                finalMessage = new String(cipher.decrypt(new SignalMessage(decodedMessage)));
//            }
//
//            Log.v(TAG, "adding message " + frame.getMessage());
//            messageManager.addMessage(conversation, finalMessage, frame.getFrom(), false);
//
//        } catch (InvalidKeyException
//                | DuplicateMessageException
//                | UntrustedIdentityException
//                | InvalidKeyIdException
//                | InvalidVersionException
//                | LegacyMessageException
//                | NoSessionException
//                | IOException e) {
//            localRealm.close();
//
//            Log.getStackTraceString(e);
//        } catch (InvalidMessageException e) {
//            localRealm.close();
//
//            Log.v(TAG, "invalid message");
//            Log.getStackTraceString(e);
//        }
//        finally {
//            localRealm.close();
//        }
//
//    }
//}
