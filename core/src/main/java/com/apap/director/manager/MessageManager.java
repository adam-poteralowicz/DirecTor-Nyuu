package com.apap.director.manager;

import com.apap.director.db.realm.model.Conversation;
import com.apap.director.db.realm.model.Message;

import java.util.ArrayList;
import java.util.Date;

import io.realm.Realm;

public class MessageManager {
    private Realm realm;
    private AccountManager accountManager;

    public MessageManager(Realm realm, AccountManager accountManager) {
        this.realm = realm;
        this.accountManager = accountManager;
    }

    public ArrayList<Message> listAllMessages(Conversation conversation) {
        return (ArrayList<Message>) realm.copyFromRealm(conversation.getMessages());
    }

    public Message getMessage(Long id) {
        Message message = realm.where(Message.class).equalTo("id", id).findFirst();
        if (message != null) {
            return message;
        } else return null;
    }

    public ArrayList<Message> getMessages(Conversation conversation) {
        if (conversation != null) {
            if (conversation.getMessages() != null) {
                return new ArrayList<>(conversation.getMessages());
            }
        }
        return null;
    }

    public boolean addMessage(Conversation conversation, String msg, String recipient) {
        if (conversation == null) return false;
        realm.beginTransaction();
            Message message = realm.createObject(Message.class, generateMessageId());
            message.setConversation(conversation);
            message.setContent(msg);
            message.setDate(new Date());
            message.setRecipient(recipient);
            message.setAccount(accountManager.getActiveAccount());
        realm.commitTransaction();
        return true;
    }

    public boolean deleteMessage(Long id) {
        Message messageToDelete = realm.where(Message.class).equalTo("id", id).findFirst();
        if (messageToDelete != null) {
            messageToDelete.deleteFromRealm();
            return true;
        } else return false;
    }

    public boolean updateMessage(Long id, Boolean ownership) {
        Message message = realm.where(Message.class).equalTo("id", id).findFirst();
        if (message == null) return false;
        realm.beginTransaction();
        if (ownership != null) {
            message.setMine(ownership);
        }
        realm.insertOrUpdate(message);
        realm.commitTransaction();
        return true;
    }

    private long generateMessageId() {
        long id;
        try {
            if (realm.where(Message.class).max("id") == null) {
                id = 0;
            } else {
                id = realm.where(Message.class).max("id").longValue() + 1;
            }
        } catch(ArrayIndexOutOfBoundsException ex) {
            id = 0;
        }
        return id;
    }
}
