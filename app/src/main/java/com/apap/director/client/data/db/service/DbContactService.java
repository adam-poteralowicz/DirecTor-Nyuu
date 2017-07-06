package com.apap.director.client.data.db.service;

import com.apap.director.client.data.db.entity.ContactEntity;

import java.util.List;

import javax.inject.Inject;

import io.realm.Realm;

/**
 * Created by Adam on 2017-07-03.
 */

public class DbContactService {

    private Realm realm;

    @Inject
    DbContactService(Realm realm) {
        this.realm = realm;
    }

    public List<ContactEntity> getContactList() {
        return realm.where(ContactEntity.class).findAll();
    }

    public ContactEntity getContactByName(String contactName) {
        return realm.where(ContactEntity.class).equalTo("name", contactName).findFirst();
    }

    public ContactEntity getContactById(Long contactId) {
        return realm.where(ContactEntity.class).equalTo("id", contactId).findFirst();
    }
}
