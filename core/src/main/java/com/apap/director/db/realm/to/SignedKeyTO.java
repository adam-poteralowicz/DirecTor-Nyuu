package com.apap.director.db.realm.to;

import android.util.Base64;

import com.apap.director.db.realm.model.SignedKey;

import org.whispersystems.libsignal.state.SignedPreKeyRecord;

import java.io.IOException;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class SignedKeyTO {

    private String keyBase64;

    private String signatureBase64;

    public String getKeyBase64() {
        return keyBase64;
    }


    public SignedKeyTO(SignedKey signedKey) {
        try {
            SignedPreKeyRecord record = new SignedPreKeyRecord(signedKey.getSerializedKey());
            this.keyBase64 = Base64.encodeToString(record.getKeyPair().getPublicKey().serialize(), Base64.NO_WRAP | Base64.URL_SAFE);
            this.signatureBase64 = Base64.encodeToString(record.getSignature(), Base64.NO_WRAP | Base64.URL_SAFE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
