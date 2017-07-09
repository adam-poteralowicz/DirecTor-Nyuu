package com.apap.director.client.data.db.service;

import com.apap.director.client.data.db.entity.MessageEntity;
import com.apap.director.client.data.db.entity.SignedKeyEntity;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by Adam on 2017-07-04.
 */

public class DbMessageService {

    private static final String ID_COLUMN = "id";

    private Realm realm;

    @Inject
    DbMessageService(Realm realm) { this.realm = realm; }

    public RealmList<MessageEntity> getMessagesByContact(Long contactId) {
        RealmResults<MessageEntity> results = realm.where(MessageEntity.class).equalTo("conversation.id", contactId).findAll();
        RealmList<MessageEntity> realmList = new RealmList<>();

        realmList.addAll(results);
        return realmList;
    }

    public void saveMessage(MessageEntity messageEntity) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(messageEntity);
        realm.commitTransaction();
    }

    public long findNextId() {
        Number lastId = realm.where(MessageEntity.class).max(ID_COLUMN).longValue();

        if (lastId == null) {
            return 0;
        } else {
            return lastId.longValue() + 1;
        }
    }
}
