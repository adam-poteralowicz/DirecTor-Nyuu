package com.apap.director.client.data.store;

import android.util.Log;

import com.apap.director.client.data.db.entity.AccountEntity;
import com.apap.director.client.data.db.entity.OneTimeKeyEntity;

import org.whispersystems.libsignal.InvalidKeyIdException;
import org.whispersystems.libsignal.state.PreKeyRecord;
import org.whispersystems.libsignal.state.PreKeyStore;

import java.io.IOException;

import javax.inject.Inject;

import io.realm.Realm;


public class PreKeyStoreImpl implements PreKeyStore {

    private Realm realm;

    @Inject
    public PreKeyStoreImpl(Realm realm) {
        this.realm = realm;
    }

    @Override
    public PreKeyRecord loadPreKey(int preKeyId) throws InvalidKeyIdException {
        Realm realm = Realm.getDefaultInstance();
        try {
            OneTimeKeyEntity preKey = realm.where(OneTimeKeyEntity.class).equalTo("oneTimeKeyId", preKeyId).findFirst();

            if (preKey == null)
                throw new InvalidKeyIdException("No such key id");

            return new PreKeyRecord(preKey.getSerializedKey());
        } catch (IOException e) {
            Log.getStackTraceString(e);
            return null;
        }
        finally {
            realm.close();
        }
    }

    @Override
    public void storePreKey(int preKeyId, PreKeyRecord record) {
        realm.beginTransaction();
            realm.where(AccountEntity.class).equalTo("active", true).findFirst();
            OneTimeKeyEntity oneTimeKey = new OneTimeKeyEntity();

            long id;
            if (realm.where(OneTimeKeyEntity.class).findFirst() == null) {
                id = 0;
            } else {
                id = realm.where(OneTimeKeyEntity.class).max("id").longValue() + 1;
            }
            oneTimeKey.setId(id);
            oneTimeKey.setOneTimeKeyId(preKeyId);
            oneTimeKey.setSerializedKey(record.serialize());
            realm.copyToRealmOrUpdate(oneTimeKey);
        realm.commitTransaction();

    }

    @Override
    public boolean containsPreKey(int preKeyId) {

        return realm.where(OneTimeKeyEntity.class).equalTo("oneTimeKeyId", preKeyId).findFirst() != null;

    }

    @Override
    public void removePreKey(int preKeyId) {

        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
            realm.where(OneTimeKeyEntity.class).equalTo("oneTimeKeyId", preKeyId).findFirst().deleteFromRealm();
        realm.commitTransaction();
        realm.close();

    }
}
