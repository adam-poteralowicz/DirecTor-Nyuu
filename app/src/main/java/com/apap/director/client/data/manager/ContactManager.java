package com.apap.director.client.data.manager;

import com.apap.director.client.data.db.entity.ContactEntity;

import javax.inject.Inject;

import io.realm.Realm;

public class ContactManager {
    private Realm realm;

    @Inject
    public ContactManager(Realm realm) {
        this.realm = realm;
    }

    public ContactEntity getContact(String name) {
        return realm.where(ContactEntity.class).equalTo("name", name).findFirst();
    }
}
