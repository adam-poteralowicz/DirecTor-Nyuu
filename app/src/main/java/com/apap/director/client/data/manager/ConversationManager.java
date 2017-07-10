package com.apap.director.client.data.manager;

import android.util.Log;

import com.apap.director.client.data.db.entity.ContactEntity;
import com.apap.director.client.data.db.entity.ConversationEntity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

public class ConversationManager {
    private Realm realm;

    @Inject
    public ConversationManager(Realm realm) {
        this.realm = realm;

    }

    public ConversationEntity createConversation() {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        ConversationEntity conversation = realm.createObject(ConversationEntity.class, generateConversationId(realm));
        realm.copyToRealmOrUpdate(conversation);
        realm.commitTransaction();

        return conversation;
    }

    public List<ConversationEntity> listAllConversations() {
        RealmResults<ConversationEntity> conversations = realm.where(ConversationEntity.class).findAll();
        return new ArrayList<>(conversations);
    }

    public ConversationEntity getConversationById(Long id) {
        ConversationEntity conversation = realm.where(ConversationEntity.class).equalTo("id", id).findFirst();
        if (conversation != null) {
            return conversation;
        } else return null;
    }

    public ConversationEntity getConversationByContactName(String contactName) {
        ConversationEntity conversation = realm.where(ConversationEntity.class).equalTo("contacts.name", contactName).findFirst();
        if (conversation != null) {
            return conversation;
        } else return null;
    }

    public ConversationEntity getConversationByContactId(Long contactId) {
        Realm realm = Realm.getDefaultInstance();
        ConversationEntity conversation = realm.where(ConversationEntity.class).equalTo("contacts.id", contactId).findFirst();
        realm.close();
        return conversation;
    }

    public ConversationEntity getConversationByAccountId(Long accountId) {
        return realm.where(ConversationEntity.class).equalTo("account.id", accountId).findFirst();
    }

    public ConversationEntity addConversation(ContactEntity contact) {
        if (contact == null)
            return null;

        Realm realm = Realm.getDefaultInstance();

        realm.beginTransaction();
        ConversationEntity conversation = realm.createObject(ConversationEntity.class, generateConversationId(realm));
        conversation.setContact(contact);
        conversation.setMessages(new RealmList<>());

        realm.copyToRealmOrUpdate(conversation);
        realm.commitTransaction();

        realm.close();
        return conversation;
    }

    public boolean deleteConversationById(Long id) {
        ConversationEntity conversationToDelete = realm.where(ConversationEntity.class).equalTo("id", id).findFirst();
        if (conversationToDelete != null) {
            realm.beginTransaction();
            conversationToDelete.deleteFromRealm();
            realm.commitTransaction();
            return true;
        } else return false;
    }

    public boolean deleteConversationByContactName(String contactName) {
        ConversationEntity conversationToDelete = realm.where(ConversationEntity.class).equalTo("contacts.name", contactName).findFirst();
        if (conversationToDelete != null) {
            realm.beginTransaction();
            conversationToDelete.deleteFromRealm();
            realm.commitTransaction();
            return true;
        } else return false;
    }

    public boolean deleteConversationByContactId(Long contactId) {
        ConversationEntity conversationToDelete = realm.where(ConversationEntity.class).equalTo("contacts.id", contactId).findFirst();
        if (conversationToDelete != null) {
            realm.beginTransaction();
            conversationToDelete.deleteFromRealm();
            realm.commitTransaction();
            return true;
        } else return false;
    }

    public boolean deleteConversationByAccountId(Long accountId) {
        ConversationEntity conversationToDelete = realm.where(ConversationEntity.class).equalTo("account.id", accountId).findFirst();
        if (conversationToDelete != null) {
            realm.beginTransaction();
            conversationToDelete.deleteFromRealm();
            realm.commitTransaction();
            return true;
        } else return false;
    }

    private long generateConversationId(Realm realm) {
        long id;
        try {
            if (realm.where(ConversationEntity.class).max("id") == null) {
                id = 0;
            } else {
                id = realm.where(ConversationEntity.class).max("id").longValue() + 1;
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            id = 0;
            Log.getStackTraceString(e);
        }
        return id;
    }

}
