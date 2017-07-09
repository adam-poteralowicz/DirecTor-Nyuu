package com.apap.director.client.data.db.service;

import com.apap.director.client.data.db.entity.ContactEntity;
import com.apap.director.client.data.db.entity.OneTimeKeyEntity;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by Adam on 2017-07-03.
 */

public class DbContactService {

    private static final String ID_COLUMN = "id";

    private Realm realm;

    @Inject
    DbContactService(Realm realm) {
        this.realm = realm;
    }

    public RealmList<ContactEntity> getContactList() {
        RealmList<ContactEntity> list = new RealmList<>();
        RealmResults<ContactEntity> results = realm.where(ContactEntity.class).findAll();

        list.addAll(results);

        return list;
    }

    public ContactEntity getContactByName(String contactName) {
        return realm.where(ContactEntity.class).equalTo("name", contactName).findFirst();
    }

    public ContactEntity getContactById(Long contactId) {
        return realm.where(ContactEntity.class).equalTo("id", contactId).findFirst();
    }

    public long findLastId() {
        Number lastId = realm.where(ContactEntity.class).max(ID_COLUMN).longValue();

        if (lastId == null) {
            return 0;
        } else {
            return lastId.longValue() + 1;
        }
    }
}
