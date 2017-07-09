package com.apap.director.client.domain.model;

import org.whispersystems.libsignal.IdentityKeyPair;
import org.whispersystems.libsignal.InvalidKeyException;
import org.whispersystems.libsignal.util.KeyHelper;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by Alicja Michniewicz
 */

@Getter
@Setter
@NoArgsConstructor
public class SignedKeyModel {

    private long id;
    private int signedKeyId;
    private byte[] serializedKey;

    public SignedKeyModel(IdentityKeyPair keyPair, int id) throws InvalidKeyException {
        KeyHelper.generateSignedPreKey(keyPair, id);
    }

}
