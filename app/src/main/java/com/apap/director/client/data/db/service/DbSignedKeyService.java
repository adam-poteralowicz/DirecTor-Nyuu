package com.apap.director.client.data.db.service;

import android.util.Log;

import com.apap.director.client.data.db.entity.AccountEntity;
import com.apap.director.client.data.db.entity.SignedKeyEntity;

import org.whispersystems.libsignal.InvalidKeyIdException;
import org.whispersystems.libsignal.state.SignedPreKeyRecord;
import org.whispersystems.libsignal.state.SignedPreKeyStore;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.realm.Realm;

/**
 * Created by Alicja Michniewicz
 */

public class DbSignedKeyService implements SignedPreKeyStore {

    private Realm realm;

    @Inject
    public DbSignedKeyService(Realm realm) {
        this.realm = realm;
    }

    @Override
    public SignedPreKeyRecord loadSignedPreKey(int signedPreKeyId) throws InvalidKeyIdException {

        try {



            return new SignedPreKeyRecord(signedKey.getSerializedKey());
        } catch (IOException e) {
            Log.getStackTraceString(e);

            throw new InvalidKeyIdException("IO exception: " + e.getMessage());
        }
    }

    @Override
    public List<SignedPreKeyRecord> loadSignedPreKeys() {
        try {
            List<SignedKeyEntity> list = realm.where(SignedKeyEntity.class).findAll();
            List<SignedPreKeyRecord> records = new ArrayList<>(list.size());

            for (SignedKeyEntity preKey : list) {
                records.add(new SignedPreKeyRecord(preKey.getSerializedKey()));
            }

            return records;
        } catch (IOException e) {
            Log.getStackTraceString(e);
            return null;
        }

    }

    @Override
    public void storeSignedPreKey(int signedPreKeyId, SignedPreKeyRecord record) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        SignedKeyEntity signedKey = new SignedKeyEntity();
        signedKey.setSerializedKey(record.serialize());
        signedKey.setSignedKeyId(signedPreKeyId);

        long id;
        if (realm.where(SignedKeyEntity.class).findFirst() == null) {
            id = 0;
        } else {
            id = realm.where(SignedKeyEntity.class).max("id").longValue() + 1;
        }
        signedKey.setId(id);
        realm.copyToRealmOrUpdate(signedKey);
        realm.commitTransaction();
        realm.close();

    }

    @Override
    public boolean containsSignedPreKey(int signedPreKeyId) {
        return realm.where(SignedKeyEntity.class).equalTo("signedKeyId", signedPreKeyId).findFirst() != null;
    }

    @Override
    public void removeSignedPreKey(int signedPreKeyId) {
        realm.where(SignedKeyEntity.class).equalTo("signedKeyId", signedPreKeyId).findFirst().deleteFromRealm();
    }

}
