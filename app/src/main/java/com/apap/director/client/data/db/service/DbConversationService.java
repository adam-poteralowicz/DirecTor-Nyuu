package com.apap.director.client.data.db.service;

import com.apap.director.client.data.db.entity.ConversationEntity;
import com.apap.director.client.data.manager.ContactManager;

import java.util.List;

import javax.inject.Inject;

import io.realm.Realm;

/**
 * Created by Adam on 2017-07-03.
 */

public class DbConversationService {

    @Inject ContactManager contactManager;

    private Realm realm;

    @Inject
    DbConversationService(Realm realm) {
        this.realm = realm;
    }

    public List<ConversationEntity> getConversationList() {
        return realm.where(ConversationEntity.class).findAll();
    }
}
