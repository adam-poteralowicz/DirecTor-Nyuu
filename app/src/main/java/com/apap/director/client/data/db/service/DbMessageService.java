package com.apap.director.client.data.db.service;

import com.apap.director.client.data.db.entity.MessageEntity;

import java.util.List;

import javax.inject.Inject;

import io.realm.Realm;

/**
 * Created by Adam on 2017-07-04.
 */

public class DbMessageService {

    private Realm realm;

    @Inject
    public DbMessageService(Realm realm) { this.realm = realm; }

    public List<MessageEntity> getMessagesByContact(Long contactId) {
        return realm.where(MessageEntity.class).equalTo("conversation.id", contactId).findAll();
    }
}
