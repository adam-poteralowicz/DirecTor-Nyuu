package com.apap.director.client.data.db.service;

import com.apap.director.client.data.db.entity.ConversationEntity;
import com.apap.director.client.data.db.entity.SignedKeyEntity;
import com.apap.director.client.data.manager.ContactManager;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by Adam on 2017-07-03.
 */

public class DbConversationService {

    private static final String ID_COLUMN = "id";

    private Realm realm;

    @Inject
    DbConversationService(Realm realm) {
        this.realm = realm;
    }

    public RealmList<ConversationEntity> getConversationList() {
        RealmList<ConversationEntity> list = new RealmList<>();
        RealmResults<ConversationEntity> results = realm.where(ConversationEntity.class).findAll();

        list.addAll(results);

        return list;
    }

    public long findNextId() {
        Number lastId = realm.where(ConversationEntity.class).max(ID_COLUMN).longValue();

        if (lastId == null) {
            return 0;
        } else {
            return lastId.longValue() + 1;
        }
    }
}
