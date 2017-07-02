package com.apap.director.client.data.store;

import android.util.Log;

import com.apap.director.db.realm.model.Account;
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
        Realm realm = Realm.getDefaultInstance();
        try {
            OneTimeKey preKey = realm.where(OneTimeKey.class).equalTo("oneTimeKeyId", preKeyId).findFirst();

            if (preKey == null)
                throw new InvalidKeyIdException("No such key id");

            return new PreKeyRecord(preKey.getSerializedKey());
        } catch (IOException e) {
            Log.getStackTraceString(e);
            return null;
        }
    }

    @Override
    public void storePreKey(int preKeyId, PreKeyRecord record) {
        realm.beginTransaction();
            realm.where(Account.class).equalTo("active", true).findFirst();
            OneTimeKey oneTimeKey = new OneTimeKey();

            long id;
            if (realm.where(OneTimeKey.class).findFirst() == null) {
                id = 0;
            } else {
                id = realm.where(OneTimeKey.class).max("id").longValue() + 1;
            }
            oneTimeKey.setId(id);
            oneTimeKey.setOneTimeKeyId(preKeyId);
            oneTimeKey.setSerializedKey(record.serialize());
            realm.copyToRealmOrUpdate(oneTimeKey);
        realm.commitTransaction();

    }

    @Override
    public boolean containsPreKey(int preKeyId) {

        return realm.where(OneTimeKey.class).equalTo("oneTimeKeyId", preKeyId).findFirst() != null;

    }

    @Override
    public void removePreKey(int preKeyId) {

        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
            realm.where(OneTimeKey.class).equalTo("oneTimeKeyId", preKeyId).findFirst().deleteFromRealm();
        realm.commitTransaction();

    }
}
