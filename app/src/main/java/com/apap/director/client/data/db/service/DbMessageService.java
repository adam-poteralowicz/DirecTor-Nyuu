package com.apap.director.client.data.db.service;

import com.apap.director.client.data.db.entity.ContactEntity;
import com.apap.director.client.data.db.entity.ConversationEntity;
import com.apap.director.client.data.db.entity.MessageEntity;

import java.util.List;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by Adam Poterałowicz
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

    public MessageEntity getMessageByMessageId(Long messageId) {
        return realm.where(MessageEntity.class).equalTo("id", messageId).findFirst();
    }

    public void saveMessage(MessageEntity messageEntity) {
        realm.beginTransaction();

        ContactEntity managedContact = realm.copyToRealmOrUpdate(messageEntity.getConversation().getContact());
        ConversationEntity managedConversation = realm.copyToRealmOrUpdate(messageEntity.getConversation());
        managedConversation.setContact(managedContact);

        MessageEntity managedMessage = realm.copyToRealmOrUpdate(messageEntity);
        List<MessageEntity> managedMessageList = realm.copyFromRealm(managedConversation.getMessages());
        managedMessageList.add(managedMessage);

        realm.commitTransaction();
    }

    public void deleteMessage(MessageEntity messageEntity) {
        realm.beginTransaction();
        messageEntity.deleteFromRealm();
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
