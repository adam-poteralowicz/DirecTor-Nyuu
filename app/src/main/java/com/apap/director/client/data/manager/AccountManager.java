package com.apap.director.client.data.manager;

import com.apap.director.client.data.db.entity.AccountEntity;

import io.realm.Realm;


public class AccountManager {

    private Realm realm;

    public AccountManager(Realm realm) {
        this.realm = realm;
    }

    public AccountEntity getActiveAccount() {
        Realm realm = Realm.getDefaultInstance();
        AccountEntity account = realm.where(AccountEntity.class).equalTo("active", true).findFirst();
        realm.close();
        return account;
    }
}
