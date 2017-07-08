package com.apap.director.client.data.net.model;

import android.util.Base64;
import android.util.Log;

import com.apap.director.client.data.db.entity.OneTimeKeyEntity;

import org.whispersystems.libsignal.state.PreKeyRecord;

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
public class OneTimeKey {

    private long keyId;
    private String keyBase64;
    private int oneTimeKeyId;

    public OneTimeKey(OneTimeKeyEntity oneTimeKey) {
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
