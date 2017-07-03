package com.apap.director.client.data.db.service;

import com.apap.director.client.data.db.entity.AccountEntity;

import java.util.List;

import javax.inject.Inject;

import io.realm.Realm;

public class DbAccountService {

    private Realm realm;

    @Inject
    public DbAccountService(Realm realm) {
        this.realm = realm;
    }

    public List<AccountEntity> getAccountList() {
        return realm.where(AccountEntity.class).findAll();
    }

}
