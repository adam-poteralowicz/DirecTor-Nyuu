package com.apap.director.client.domain.util;

import android.util.Base64;
import android.util.Log;

import com.apap.director.client.data.net.model.Message;
import com.apap.director.client.data.net.to.MessageTO;
import com.apap.director.client.data.store.PreKeyStoreImpl;
import com.apap.director.client.data.store.SessionStoreImpl;
import com.apap.director.client.domain.model.ContactKeyModel;
import com.apap.director.client.domain.model.MessageModel;

import org.whispersystems.curve25519.Curve25519;
import org.whispersystems.libsignal.DuplicateMessageException;
import org.whispersystems.libsignal.IdentityKey;
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
import org.whispersystems.libsignal.protocol.CiphertextMessage;
import org.whispersystems.libsignal.protocol.PreKeySignalMessage;
import org.whispersystems.libsignal.protocol.SignalMessage;
import org.whispersystems.libsignal.state.IdentityKeyStore;
import org.whispersystems.libsignal.state.PreKeyStore;
import org.whispersystems.libsignal.state.SessionRecord;
import org.whispersystems.libsignal.state.SessionState;
import org.whispersystems.libsignal.state.SessionStore;
import org.whispersystems.libsignal.state.SignedPreKeyStore;

/**
 * Created by Alicja Michniewicz
 */

public class EncryptionService {

    private static final String TAG = EncryptionService.class.getSimpleName();

    private Curve25519 curve;
    private IdentityKeyStore identityKeyStore;
    private SignedPreKeyStore signedPreKeyStore;
    private SessionStore sessionStore;
    private PreKeyStore preKeyStore;

    public EncryptionService(Curve25519 curve, IdentityKeyStore identityKeyStore, SignedPreKeyStore signedPreKeyStore, SessionStore sessionStore, PreKeyStore preKeyStore) {
        this.curve = curve;
        this.identityKeyStore = identityKeyStore;
        this.signedPreKeyStore = signedPreKeyStore;
        this.sessionStore = sessionStore;
        this.preKeyStore = preKeyStore;
    }

    public String signMessage(IdentityKeyPair keyPair, String message) {
        return new String(curve.calculateSignature(keyPair.getPrivateKey().serialize(), message.getBytes()));
    }

    public MessageModel encryptMessage(IdentityKeyPair keyPair, MessageModel message) {
        // TODO
        return null;
    }

    public MessageModel decryptMessage(ContactKeyModel contactKey, MessageModel message, int messageType) throws NoSessionException, UntrustedIdentityException, LegacyMessageException, InvalidVersionException, InvalidMessageException, DuplicateMessageException, InvalidKeyException, InvalidKeyIdException {
        SignalProtocolAddress address = new SignalProtocolAddress(contactKey.getKeyBase64(), contactKey.getDeviceId());
        byte[] decodedMessage = Base64.decode(message.getContent(), Base64.NO_WRAP | Base64.URL_SAFE);

        message.setContent(decryptText(address, decodedMessage, messageType));

        return message;
    }

    private String decryptText(SignalProtocolAddress address, byte[] decodedMessage, int messageType) throws InvalidVersionException, InvalidMessageException, InvalidKeyException, DuplicateMessageException, InvalidKeyIdException, UntrustedIdentityException, LegacyMessageException, NoSessionException {
        SessionCipher cipher = new SessionCipher(sessionStore, preKeyStore, signedPreKeyStore, identityKeyStore, address);

        if (messageType == CiphertextMessage.PREKEY_TYPE) {
            return new String(cipher.decrypt(new PreKeySignalMessage(decodedMessage)));
        } else {
            return new String(cipher.decrypt(new SignalMessage(decodedMessage)));
        }
    }
}

