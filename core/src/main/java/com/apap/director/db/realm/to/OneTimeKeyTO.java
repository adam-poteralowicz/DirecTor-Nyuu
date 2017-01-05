package com.apap.director.db.realm.to;

import com.apap.director.db.realm.model.OneTimeKey;

public class OneTimeKeyTO {

    private long keyId;
    private String keyBase64;
    private OneTimeKey oneTimeKey;


    public long getKeyId() {
        return keyId;
    }

    public String getKeyBase64() {
        return keyBase64;
    }

    public OneTimeKeyTO(OneTimeKey oneTimeKey) {
        this.oneTimeKey = oneTimeKey;
    }
}
