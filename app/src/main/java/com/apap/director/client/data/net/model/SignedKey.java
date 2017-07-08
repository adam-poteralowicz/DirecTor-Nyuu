package com.apap.director.client.data.net.model;

import android.util.Base64;
import android.util.Log;

import com.apap.director.client.data.db.entity.SignedKeyEntity;

import org.whispersystems.libsignal.state.SignedPreKeyRecord;

import java.io.IOException;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by Adam Poterałowicz
 */

@Getter
@Setter
@NoArgsConstructor
public class SignedKey {

    private String keyBase64;
    private String signatureBase64;
    private int signedKeyId;

    public SignedKey(SignedKeyEntity signedKey) {
        try {
            SignedPreKeyRecord record = new SignedPreKeyRecord(signedKey.getSerializedKey());
            this.keyBase64 = Base64.encodeToString(record.getKeyPair().getPublicKey().serialize(), Base64.NO_WRAP | Base64.URL_SAFE);
            this.signatureBase64 = Base64.encodeToString(record.getSignature(), Base64.NO_WRAP | Base64.URL_SAFE);
            this.signedKeyId = signedKey.getSignedKeyId();
        } catch (IOException e) {
            Log.getStackTraceString(e);
        }
    }
}
