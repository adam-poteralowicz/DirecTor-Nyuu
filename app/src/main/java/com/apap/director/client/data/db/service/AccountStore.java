package com.apap.director.client.data.db.service;

import android.util.Base64;
import android.util.Log;

import com.apap.director.client.data.db.entity.AccountEntity;
import com.apap.director.client.data.db.mapper.AccountMapper;

import org.whispersystems.libsignal.state.SignedPreKeyRecord;

import java.io.IOException;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

public class AccountStore {

    @Inject
    AccountMapper accountMapper;

    private final String ACTIVE_COLUMN = "active";
    private final String ID_COLUMN = "keyBase64";
    private final String NAME_COLUMN = "name";
    private final String SIGNED_KEY_COLUMN = "signedKey";
    private final String SIGNED_KEY_ID_COLUMN = "signedKeyId";
    private final String PRE_KEY_COLUMN = "oneTimeKeys";
    private final String PRE_KEY_ID_COLUMN = "oneTimeKeyId";

    private Realm realm;

    @Inject
    public AccountStore(Realm realm) {
        this.realm = realm;
    }

    public RealmList<AccountEntity> getAccountList() {
        RealmList <AccountEntity> list = new RealmList<>();
        RealmResults<AccountEntity> results = realm.where(AccountEntity.class).findAll();
        list.addAll(results.subList(0, results.size()));

        return list;
    }

    public AccountEntity getActiveAccount() {
        return realm.where(AccountEntity.class).equalTo(ACTIVE_COLUMN, true).findFirst();
    }

    public int findLastSignedKeyId(String accountId) {
        Number lastId = realm.where(AccountEntity.class).equalTo(ID_COLUMN, accountId).max(String.format("%s.%s", SIGNED_KEY_COLUMN, SIGNED_KEY_ID_COLUMN));

        if (lastId == null) {
            return 0;
        } else {
            return lastId.intValue() + 1;
        }
    }

    public int findLastOneTimeKeyId(String accountId) {
        Number lastId = realm.where(AccountEntity.class).equalTo(ID_COLUMN, accountId).max(String.format("%s.%s", PRE_KEY_COLUMN, PRE_KEY_ID_COLUMN));

        if (lastId == null) {
            return 0;
        } else {
            return lastId.intValue() + 1;
        }
    }

    public AccountEntity findAccountByName(String name) {
        return realm.where(AccountEntity.class).equalTo(NAME_COLUMN, name).findFirst();
    }

    public void saveAccount(AccountEntity accountEntity) {
        realm.beginTransaction();
        realm.copyToRealm(accountEntity);
        realm.commitTransaction();
    }

    public void updateAccount(AccountEntity accountEntity) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(accountEntity);
        realm.commitTransaction();
    }

    public void setAccountActive(AccountEntity accountEntity) {
        realm.beginTransaction();
        accountEntity.setActive(true);
        realm.copyToRealmOrUpdate(accountEntity);
        realm.commitTransaction();
    }

    public String getSignedKeySignature(AccountEntity accountEntity) {
        SignedPreKeyRecord record = null;
        try {
            record = new SignedPreKeyRecord(accountEntity.getSignedKey().getSerializedKey());
        } catch (IOException e) {
            Log.getStackTraceString(e);
        }

        return Base64.encodeToString(record.getSignature(), Base64.NO_WRAP | Base64.URL_SAFE);
    }
}
