package com.apap.director.client.domain.model;

import android.util.Base64;

import com.apap.director.client.domain.util.Base64Util;

import org.whispersystems.libsignal.IdentityKey;
import org.whispersystems.libsignal.IdentityKeyPair;
import org.whispersystems.libsignal.InvalidKeyException;
import org.whispersystems.libsignal.state.SignedPreKeyRecord;
import org.whispersystems.libsignal.util.ByteUtil;
import org.whispersystems.libsignal.util.KeyHelper;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * Created by Alicja Michniewicz
 */

@Getter
@Setter
@NoArgsConstructor
public class AccountModel {

    private String keyBase64;
    private String name;
    private byte[] keyPair;
    private int registrationId;
    private String masterPassword;
    private List<OneTimeKeyModel> oneTimeKeys;
    private SignedKeyModel signedKey;
    private boolean registered;
    private boolean active;
    private String cookie;

    public void preconfigureAccount() {
        IdentityKeyPair identityKeyPair = KeyHelper.generateIdentityKeyPair();

        keyPair = identityKeyPair.serialize();
        keyBase64 = Base64Util.convertToBase64(identityKeyPair.getPublicKey());

        registrationId = KeyHelper.generateRegistrationId(false);
    }

}
