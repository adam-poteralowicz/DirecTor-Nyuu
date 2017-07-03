package com.apap.director.client.data.manager;

import android.util.Log;

import com.apap.director.client.data.db.entity.AccountEntity;
import com.apap.director.client.data.db.entity.ConversationEntity;
import com.apap.director.client.data.db.entity.MessageEntity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;

public class MessageManager {
    private Realm realm;
    private String TAG = this.getClass().getSimpleName();

    public MessageManager(Realm realm, AccountManager accountManager) {
        this.realm = realm;
    }

    public List<MessageEntity> listAllMessages(ConversationEntity conversation) {
        return realm.copyFromRealm(conversation.getMessages());
    }

    public MessageEntity getMessage(Long id) {
        MessageEntity message = realm.where(MessageEntity.class).equalTo("id", id).findFirst();
        if (message != null) {
            return message;
        } else
            return null;
    }

    public List<MessageEntity> getMessages(ConversationEntity conversation) {
        if (conversation.getMessages() != null) {
            return new ArrayList<>(conversation.getMessages());
        }
        return null;
    }

    public MessageEntity addMessage(ConversationEntity conv, String msg, String recipient, Boolean owned) {

        Realm realm = Realm.getDefaultInstance();
        Log.v(TAG, "adding message "+msg+" owned: "+owned);
        if (conv == null) {
            realm.close();
            return null;
        }
        realm.beginTransaction();
        ConversationEntity conversation = realm.where(ConversationEntity.class).equalTo("id", conv.getId()).findFirst();
        MessageEntity message = realm.createObject(MessageEntity.class, generateMessageId(realm));
        message.setConversation(conv);
        message.setContent(msg);
        message.setDate(new Date());
        message.setRecipient(recipient);
        message.setAccount(realm.where(AccountEntity.class).equalTo("active", true).findFirst());
        message.setMine(owned);
        RealmList<MessageEntity> conversationMsg = conversation.getMessages();
        conversationMsg.add(message);
        conversation.setMessages(conversationMsg);
        realm.copyToRealmOrUpdate(message);
        realm.copyToRealmOrUpdate(conversation);
        realm.commitTransaction();
        realm.close();
        return message;
    }

    public boolean deleteMessage(Long id) {
        Realm realm = Realm.getDefaultInstance();
        MessageEntity messageToDelete = realm.where(MessageEntity.class).equalTo("id", id).findFirst();
        realm.close();
        if (messageToDelete != null) {
            realm.beginTransaction();
            messageToDelete.deleteFromRealm();
            realm.commitTransaction();
            return true;
        } else return false;
    }

    public boolean updateMessage(Long id, Boolean ownership) {
        MessageEntity message = realm.where(MessageEntity.class).equalTo("id", id).findFirst();
        if (message == null)
            return false;
        realm.beginTransaction();
        if (ownership != null) {
            message.setMine(ownership);
        }
        realm.insertOrUpdate(message);
        realm.commitTransaction();
        return true;
    }

    private long generateMessageId(Realm realm) {
        long id;
        try {
            if (realm.where(MessageEntity.class).max("id") == null) {
                id = 0;
            } else {
                id = realm.where(MessageEntity.class).max("id").longValue() + 1;
            }
        } catch(ArrayIndexOutOfBoundsException e) {
            id = 0;
            Log.getStackTraceString(e);
        }
        return id;
    }
}
