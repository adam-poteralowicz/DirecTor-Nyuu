package com.apap.director.client.data.net.to;

import android.util.Base64;
import android.util.Log;


import com.apap.director.client.data.db.entity.OneTimeKeyEntity;

import org.whispersystems.libsignal.state.PreKeyRecord;

import java.io.IOException;

import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
public class OneTimeKeyTO {

    private long keyId;
    private String keyBase64;
    private int oneTimeKeyId;


    public long getKeyId() {
        return keyId;
    }

    public String getKeyBase64() {
        return keyBase64;
    }

    public int getOneTimeKeyId() {
        return oneTimeKeyId;
    }

    public OneTimeKeyTO(OneTimeKeyEntity oneTimeKey) {
        try {
            this.keyId = oneTimeKey.getId();
            PreKeyRecord record = new PreKeyRecord(oneTimeKey.getSerializedKey());
            this.keyBase64 = Base64.encodeToString(record.getKeyPair().getPublicKey().serialize(), Base64.NO_WRAP | Base64.URL_SAFE);
            this.oneTimeKeyId = oneTimeKey.getOneTimeKeyId();
        } catch (IOException e) {
            Log.getStackTraceString(e);
        }
    }
}
