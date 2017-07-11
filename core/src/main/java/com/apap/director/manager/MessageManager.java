package com.apap.director.manager;

import android.util.Log;

import com.apap.director.db.realm.model.Account;
import com.apap.director.db.realm.model.Conversation;
import com.apap.director.db.realm.model.Message;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;

public class MessageManager {
    private Realm realm;
    private AccountManager accountManager;

    public MessageManager(Realm realm, AccountManager accountManager) {
        this.realm = realm;
        this.accountManager = accountManager;
    }

    public List<Message> listAllMessages(Conversation conversation) {
        return (ArrayList<Message>) realm.copyFromRealm(conversation.getMessages());
    }

    public Message getMessage(Long id) {
        Message message = realm.where(Message.class).equalTo("id", id).findFirst();
        if (message != null) {
            return message;
        } else return null;
    }

    public List<Message> getMessages(Conversation conversation) {
        if (conversation != null && conversation.getMessages() != null) {
            return new ArrayList<>(conversation.getMessages());
        }
        return null;
    }


    public Message addMessage(Conversation conv, String msg, String recipient, Boolean owned) {

        Realm realm = Realm.getDefaultInstance();
        Log.v(getClass().getSimpleName(), "adding mesage " + msg + " owned: " + owned);
        if (conv == null)
            return null;
        realm.beginTransaction();
        Conversation conversation = realm.where(Conversation.class).equalTo("id", conv.getId()).findFirst();
        Message message = realm.createObject(Message.class, generateMessageId(realm));
        message.setConversation(conv);
        message.setContent(msg);
        message.setDate(new Date());
        message.setRecipient(recipient);
        message.setAccount(realm.where(Account.class).equalTo("active", true).findFirst());
        message.setMine(owned);
        RealmList<Message> conversationMsg = conversation.getMessages();
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
        Message messageToDelete = realm.where(Message.class).equalTo("id", id).findFirst();
        if (messageToDelete != null) {
            realm.beginTransaction();
            messageToDelete.deleteFromRealm();
            realm.commitTransaction();
            return true;
        } else return false;
    }

    public boolean updateMessage(Long id, Boolean ownership) {
        Message message = realm.where(Message.class).equalTo("id", id).findFirst();
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
            if (realm.where(Message.class).max("id") == null) {
                id = 0;
            } else {
                id = realm.where(Message.class).max("id").longValue() + 1;
            }
        } catch (ArrayIndexOutOfBoundsException ex) {
            Log.getStackTraceString(ex);
            id = 0;
        }
        return id;
    }
}
