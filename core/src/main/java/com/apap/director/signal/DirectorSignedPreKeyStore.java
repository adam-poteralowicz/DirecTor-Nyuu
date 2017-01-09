package com.apap.director.signal;

import android.util.Base64;

import com.apap.director.db.realm.model.Account;
import com.apap.director.db.realm.model.SignedKey;

import org.whispersystems.libsignal.InvalidKeyIdException;
import org.whispersystems.libsignal.state.SignedPreKeyRecord;
import org.whispersystems.libsignal.state.SignedPreKeyStore;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.realm.Realm;

public class DirectorSignedPreKeyStore implements SignedPreKeyStore {

    private Realm realm;

    @Inject
    public DirectorSignedPreKeyStore(Realm realm) {
        this.realm = realm;
    }

    @Override
    public SignedPreKeyRecord loadSignedPreKey(int signedPreKeyId) throws InvalidKeyIdException {
        return null;
    }

    @Override
    public List<SignedPreKeyRecord> loadSignedPreKeys() {
        try {
            List<SignedKey> list = realm.where(SignedKey.class).findAll();
            List<SignedPreKeyRecord> records = new ArrayList<>(list.size());

            for(SignedKey preKey : list){
                    records.add(new SignedPreKeyRecord(preKey.getSerializedKey()));
            }

            return records;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public void storeSignedPreKey(int signedPreKeyId, SignedPreKeyRecord record) {
        realm.beginTransaction();
            SignedKey signedKey = new SignedKey();
            signedKey.setSerializedKey(record.serialize());
            signedKey.setSignedKeyId(signedPreKeyId);

            long id;
            if(realm.where(SignedKey.class).findFirst() == null){
                id = 0;
            }
            else{
                id = realm.where(SignedKey.class).max("id").longValue()+1;
            }
            signedKey.setId(id);
        realm.copyToRealmOrUpdate(signedKey);
        realm.commitTransaction();

    }

    @Override
    public boolean containsSignedPreKey(int signedPreKeyId) {
        return realm.where(SignedKey.class).equalTo("signedKeyId", signedPreKeyId).findFirst() == null ? false : true;
    }

    @Override
    public void removeSignedPreKey(int signedPreKeyId) {
        realm.where(SignedKey.class).equalTo("signedKeyId", signedPreKeyId).findFirst().deleteFromRealm();
    }


}
