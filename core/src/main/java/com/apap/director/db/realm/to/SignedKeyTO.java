package com.apap.director.db.realm.to;

import com.apap.director.db.realm.model.SignedKey;

public class SignedKeyTO {

    private String keyBase64;
    private SignedKey signedKey;

    public String getKeyBase64() {
        return keyBase64;
    }

    public SignedKeyTO(SignedKey signedKey) {
        this.signedKey = signedKey;
    }
}
