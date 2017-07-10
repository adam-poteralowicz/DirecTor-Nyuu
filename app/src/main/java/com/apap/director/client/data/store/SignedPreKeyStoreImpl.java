package com.apap.director.client.data.store;

import android.util.Log;

import com.apap.director.client.data.db.entity.SignedKeyEntity;

import org.whispersystems.libsignal.InvalidKeyIdException;
import org.whispersystems.libsignal.state.SignedPreKeyRecord;
import org.whispersystems.libsignal.state.SignedPreKeyStore;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.realm.Realm;

public class SignedPreKeyStoreImpl implements SignedPreKeyStore {

    private static final String ID_COLUMN = "signedKeyId";
    private Realm realm;

    @Inject
    public SignedPreKeyStoreImpl(Realm realm) {
        this.realm = realm;
    }

    @Override
    public SignedPreKeyRecord loadSignedPreKey(int signedPreKeyId) throws InvalidKeyIdException {
        SignedKeyEntity signedKey = findKeyById(signedPreKeyId);

        if (signedKey == null) {
            throw new InvalidKeyIdException("No such key " + signedPreKeyId);
        }

        return deserializeRecord(signedKey);
    }

    @Override
    public List<SignedPreKeyRecord> loadSignedPreKeys() {
        List<SignedKeyEntity> list = realm.where(SignedKeyEntity.class).findAll();
        List<SignedPreKeyRecord> records = new ArrayList<>(list.size());

        for (SignedKeyEntity preKey : list) {
            records.add(deserializeRecord(preKey));
        }

        return records;
    }

    @Override
    public void storeSignedPreKey(int signedPreKeyId, SignedPreKeyRecord record) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(createSignedKeyEntity(record, signedPreKeyId));
        realm.commitTransaction();
    }

    @Override
    public boolean containsSignedPreKey(int signedPreKeyId) {
        return realm.where(SignedKeyEntity.class).equalTo(ID_COLUMN, signedPreKeyId).findFirst() != null;
    }

    @Override
    public void removeSignedPreKey(int signedPreKeyId) {
        realm.where(SignedKeyEntity.class).equalTo(ID_COLUMN, signedPreKeyId).findFirst().deleteFromRealm();
    }

    private SignedKeyEntity findKeyById(long id) {
        return realm.where(SignedKeyEntity.class)
                .equalTo(ID_COLUMN, id)
                .findFirst();
    }

    private long generateSignedKeyId() {
        Number lastId = realm.where(SignedKeyEntity.class).max(ID_COLUMN).longValue();

        if (lastId == null) {
            return 0;
        } else {
            return lastId.longValue() + 1;
        }
    }

    private SignedPreKeyRecord deserializeRecord(SignedKeyEntity key) {
        try {
            return new SignedPreKeyRecord(key.getSerializedKey());
        } catch (IOException e) {
            Log.e(SignedPreKeyStoreImpl.class.getSimpleName(), "Serialization for signed key failed", e);
            return null;
        }
    }

    public SignedKeyEntity createSignedKeyEntity(SignedPreKeyRecord record, int signedPreKeyId) {
        SignedKeyEntity signedKey = new SignedKeyEntity();

        signedKey.setSerializedKey(record.serialize());
        signedKey.setSignedKeyId(signedPreKeyId);
        signedKey.setId(generateSignedKeyId());

        return signedKey;
    }
}
