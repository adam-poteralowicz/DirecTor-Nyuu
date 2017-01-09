package com.apap.director.db.realm.to;

import android.util.Base64;

import com.apap.director.db.realm.model.OneTimeKey;

import org.whispersystems.libsignal.state.PreKeyRecord;

import java.io.IOException;

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
        try {
            this.keyId = oneTimeKey.getId();
            PreKeyRecord record = new PreKeyRecord(oneTimeKey.getSerializedKey());
            this.keyBase64 = Base64.encodeToString(oneTimeKey.getSerializedKey(), Base64.NO_WRAP | Base64.URL_SAFE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
