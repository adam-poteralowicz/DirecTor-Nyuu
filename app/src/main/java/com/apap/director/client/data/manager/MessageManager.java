package com.apap.director.client.data.manager;

import android.util.Log;

import com.apap.director.client.data.db.entity.ConversationEntity;
import com.apap.director.client.data.db.entity.MessageEntity;

import java.util.Date;

import io.realm.Realm;
import io.realm.RealmList;

public class MessageManager {
    private Realm realm;
    private String TAG = this.getClass().getSimpleName();

    public MessageManager(Realm realm) {
        this.realm = realm;
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
