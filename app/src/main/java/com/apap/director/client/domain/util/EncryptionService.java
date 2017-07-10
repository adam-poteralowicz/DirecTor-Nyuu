package com.apap.director.client.domain.util;

import android.util.Base64;

import com.apap.director.client.data.net.model.OneTimeKey;
import com.apap.director.client.data.net.model.SignedKey;
import com.apap.director.client.data.net.to.MessageTO;
import com.apap.director.client.data.net.to.OneTimeKeyTO;
import com.apap.director.client.data.net.to.SignedKeyTO;
import com.apap.director.client.domain.model.ContactKeyModel;
import com.apap.director.client.domain.model.MessageModel;
import com.apap.director.client.domain.model.SessionModel;
import com.apap.director.client.domain.model.SignedKeyModel;
import com.fasterxml.jackson.databind.ObjectMapper;

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
import org.whispersystems.libsignal.SessionBuilder;
import org.whispersystems.libsignal.SessionCipher;
import org.whispersystems.libsignal.SignalProtocolAddress;
import org.whispersystems.libsignal.UntrustedIdentityException;
import org.whispersystems.libsignal.ecc.Curve;
import org.whispersystems.libsignal.ecc.ECPublicKey;
import org.whispersystems.libsignal.protocol.CiphertextMessage;
import org.whispersystems.libsignal.protocol.PreKeySignalMessage;
import org.whispersystems.libsignal.protocol.SignalMessage;
import org.whispersystems.libsignal.state.IdentityKeyStore;
import org.whispersystems.libsignal.state.PreKeyBundle;
import org.whispersystems.libsignal.state.PreKeyStore;
import org.whispersystems.libsignal.state.SessionRecord;
import org.whispersystems.libsignal.state.SessionStore;
import org.whispersystems.libsignal.state.SignedPreKeyStore;

/**
 * Created by Alicja Michniewicz
 */

public class EncryptionService {

    private static final String TAG = EncryptionService.class.getSimpleName();
    private static int IRRELEVANT_ARG = 0;

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

    public MessageTO encryptMessage(ContactKeyModel contactKey, MessageModel message) throws UnsupportedEncodingException {
        SignalProtocolAddress address = new SignalProtocolAddress(contactKey.getKeyBase64(), contactKey.getDeviceId());
        SessionCipher sessionCipher = new SessionCipher(sessionStore, preKeyStore, signedPreKeyStore, identityKeyStore, address);

        CiphertextMessage encryptedMessage = sessionCipher.encrypt(message.getContent().getBytes("UTF-8"));
        String encodedText = Base64.encodeToString(encryptedMessage.serialize(), Base64.URL_SAFE | Base64.NO_WRAP);

        return new  MessageTO(null, encodedText, encryptedMessage.getType());
    }

    public void buildSession(ContactKeyModel contactKey, OneTimeKeyTO oneTimeKey, SignedKeyTO signedKey) throws InvalidKeyException, UntrustedIdentityException {
        SignalProtocolAddress address = new SignalProtocolAddress(contactKey.getKeyBase64(), contactKey.getDeviceId());
        SessionBuilder sessionBuilder = new SessionBuilder(sessionStore, preKeyStore, signedPreKeyStore, identityKeyStore, address);

        sessionBuilder.process(buildPreKeyBundle(contactKey, oneTimeKey, signedKey));
    }

    public MessageModel decryptMessage(ContactKeyModel contactKey, MessageModel message, int messageType) throws NoSessionException, UntrustedIdentityException, LegacyMessageException, InvalidVersionException, InvalidMessageException, DuplicateMessageException, InvalidKeyException, InvalidKeyIdException {
        SignalProtocolAddress address = new SignalProtocolAddress(contactKey.getKeyBase64(), contactKey.getDeviceId());
        byte[] decodedMessage = Base64Util.convertToBytes(message.getContent());

        message.setContent(decryptText(address, decodedMessage, messageType));

        return message;
    }

    private PreKeyBundle buildPreKeyBundle(ContactKeyModel contactKey, OneTimeKeyTO preKey, SignedKeyTO signedKey) throws InvalidKeyException {
        ECPublicKey signedKeyEC = getPublicKey(preKey.getKeyBase64());
        ECPublicKey preKeyEC = getPublicKey(preKey.getKeyBase64());

        byte[] signature = getSignature(signedKey);
        IdentityKey contactIdentity = new IdentityKey(contactKey.getSerialized(), 0);

        return new PreKeyBundle(IRRELEVANT_ARG, IRRELEVANT_ARG, preKey.getOneTimeKeyId(), preKeyEC, signedKey.getSignedKeyId(), signedKeyEC, signature, contactIdentity);
    }

    private ECPublicKey getPublicKey(String keyBase64) throws InvalidKeyException {
        byte[] decodedKey = Base64Util.convertToBytes(keyBase64);
        return Curve.decodePoint(decodedKey, 0);
    }

    private byte[] getSignature(SignedKeyTO signedKeyTO) {
        return Base64Util.convertToBytes(signedKeyTO.getSignatureBase64());
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

