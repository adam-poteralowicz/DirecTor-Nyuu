package com.apap.director.im.signal;


import com.apap.director.db.dao.model.DbSignedPreKey;
import com.apap.director.db.manager.DatabaseManager;

import org.whispersystems.libsignal.InvalidKeyIdException;
import org.whispersystems.libsignal.state.SignedPreKeyRecord;
import org.whispersystems.libsignal.state.SignedPreKeyStore;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class DirectorSignedPreKeyStore implements SignedPreKeyStore {

    @Inject
    public DatabaseManager manager;

    @Override
    public SignedPreKeyRecord loadSignedPreKey(int signedPreKeyId) throws InvalidKeyIdException {
        return null;
    }

    @Override
    public List<SignedPreKeyRecord> loadSignedPreKeys() {
        try {
            List<DbSignedPreKey> list = manager.listDbSignedPreKeys();
            List<SignedPreKeyRecord> records = new ArrayList<>(list.size());

            for(DbSignedPreKey preKey : list){
                    records.add(new SignedPreKeyRecord(preKey.getSerialized()));
            }

            return records;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public void storeSignedPreKey(int signedPreKeyId, SignedPreKeyRecord record) {
        DbSignedPreKey dbSignedPreKey = new DbSignedPreKey();
        dbSignedPreKey.setSerialized(record.serialize());
        dbSignedPreKey.setDbSignedPreKeyId(signedPreKeyId);

        manager.insertOrUpdateDbSignedPreKey(dbSignedPreKey);

    }

    @Override
    public boolean containsSignedPreKey(int signedPreKeyId) {
        return manager.getDbSignedPreKeyByDbSignedPreKeyId(signedPreKeyId) == null ? false : true;
    }

    @Override
    public void removeSignedPreKey(int signedPreKeyId) {
        manager.deleteDbSignedPreKeyByDbSignedPreKeyId(signedPreKeyId);
    }


}
