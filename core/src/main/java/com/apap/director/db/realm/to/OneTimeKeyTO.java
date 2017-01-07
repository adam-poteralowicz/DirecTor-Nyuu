package com.apap.director.db.realm.to;

import android.util.Base64;

import com.apap.director.db.realm.model.OneTimeKey;

import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
public class OneTimeKeyTO {

    private long keyId;
    private String keyBase64;


    public long getKeyId() {
        return keyId;
    }

    public String getKeyBase64() {
        return keyBase64;
    }

    public OneTimeKeyTO(OneTimeKey oneTimeKey) {
        this.keyId = oneTimeKey.getId();
        this.keyBase64 = Base64.encodeToString(oneTimeKey.getSerializedKey(), Base64.NO_WRAP);
    }
}
