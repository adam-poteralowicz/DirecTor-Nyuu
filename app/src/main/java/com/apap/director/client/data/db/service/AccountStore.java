package com.apap.director.client.data.db.service;

import com.apap.director.client.data.db.entity.AccountEntity;
import com.apap.director.client.domain.model.AccountModel;
import com.apap.director.client.domain.util.Base64Util;

import org.whispersystems.libsignal.IdentityKeyPair;
import org.whispersystems.libsignal.util.KeyHelper;

import java.util.List;

import javax.inject.Inject;

import io.realm.Realm;

public class AccountStore {

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

    public List<AccountEntity> getAccountList() {
        return realm.where(AccountEntity.class).findAll();
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
}
