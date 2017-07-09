package com.apap.director.client.data.db.service;

import com.apap.director.client.data.db.entity.MessageEntity;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by Adam on 2017-07-04.
 */

public class DbMessageService {

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
        realm.copyToRealm(messageEntity);
        realm.commitTransaction();
    }
}
