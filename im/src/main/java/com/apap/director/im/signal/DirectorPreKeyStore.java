package com.apap.director.im.signal;

import com.apap.director.db.dao.model.DbPreKey;
import com.apap.director.db.manager.DatabaseManager;

import org.whispersystems.libsignal.InvalidKeyIdException;
import org.whispersystems.libsignal.state.PreKeyRecord;
import org.whispersystems.libsignal.state.PreKeyStore;

import java.io.IOException;

import javax.inject.Inject;



public class DirectorPreKeyStore implements PreKeyStore {

    @Inject
    public DatabaseManager manager;

    @Override
    public PreKeyRecord loadPreKey(int preKeyId) throws InvalidKeyIdException {
        try {
            DbPreKey preKey = manager.getDbPreKeyByDbPreKeyId(preKeyId);

            if ( preKey == null ) throw new InvalidKeyIdException("No such key id");

            return new PreKeyRecord(preKey.getSerialized());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void storePreKey(int preKeyId, PreKeyRecord record) {
        DbPreKey preKey = new DbPreKey();
        preKey.setSerialized(record.serialize());
        preKey.setDbPreKeyId(preKeyId);
        manager.insertOrUpdateDbPreKey(preKey);

    }

    @Override
    public boolean containsPreKey(int preKeyId) {

        return manager.getDbPreKeyByDbPreKeyId(preKeyId) == null ? false : true;

    }

    @Override
    public void removePreKey(int preKeyId) {

        manager.deleteDbPreKeyByDbPreKeyId(preKeyId);

    }
}
