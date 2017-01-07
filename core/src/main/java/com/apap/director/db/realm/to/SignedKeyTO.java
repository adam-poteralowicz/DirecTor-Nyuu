package com.apap.director.db.realm.to;

import android.util.Base64;

import com.apap.director.db.realm.model.SignedKey;

import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
public class SignedKeyTO {

    private String keyBase64;

    public String getKeyBase64() {
        return keyBase64;
    }

    public SignedKeyTO(SignedKey signedKey) {
        this.keyBase64 = Base64.encodeToString(signedKey.getSerializedKey(), Base64.NO_WRAP);
    }
}
