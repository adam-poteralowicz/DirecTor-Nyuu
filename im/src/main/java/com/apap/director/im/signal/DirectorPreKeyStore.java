package com.apap.director.im.signal;

import com.apap.director.db.realm.model.OneTimeKey;

import org.whispersystems.libsignal.InvalidKeyIdException;
import org.whispersystems.libsignal.state.PreKeyRecord;
import org.whispersystems.libsignal.state.PreKeyStore;

import java.io.IOException;

import javax.inject.Inject;

import io.realm.Realm;


public class DirectorPreKeyStore implements PreKeyStore {


    private Realm realm;

    @Inject
    public DirectorPreKeyStore(Realm realm) {
        this.realm = realm;
    }

    @Override
    public PreKeyRecord loadPreKey(int preKeyId) throws InvalidKeyIdException {
        try {
            OneTimeKey preKey = realm.where(OneTimeKey.class).equalTo("oneTimeKeyId", preKeyId).findFirst();

            if ( preKey == null ) throw new InvalidKeyIdException("No such key id");

            return new PreKeyRecord(preKey.getSerializedKey());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void storePreKey(int preKeyId, PreKeyRecord record) {
        realm.beginTransaction();
            OneTimeKey oneTimeKey = realm.createObject(OneTimeKey.class);
            oneTimeKey.setSerializedKey(record.serialize());
            oneTimeKey.setOneTimeKeyId(preKeyId);
        realm.commitTransaction();

    }

    @Override
    public boolean containsPreKey(int preKeyId) {

        return realm.where(OneTimeKey.class).equalTo("oneTimeKeyId", preKeyId).findFirst() == null ? false : true;

    }

    @Override
    public void removePreKey(int preKeyId) {

        realm.beginTransaction();
            realm.where(OneTimeKey.class).equalTo("oneTimeKeyId", preKeyId).findFirst().deleteFromRealm();
        realm.commitTransaction();

    }
}
