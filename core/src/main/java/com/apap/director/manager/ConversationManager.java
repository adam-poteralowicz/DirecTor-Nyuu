package com.apap.director.manager;

import com.apap.director.db.realm.model.Contact;
import com.apap.director.db.realm.model.Conversation;
import com.apap.director.db.realm.model.Message;
import com.apap.director.db.realm.model.Session;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import io.realm.annotations.PrimaryKey;

public class ConversationManager {
    private Realm realm;
    private AccountManager accountManager;

    public ConversationManager(Realm realm, AccountManager accountManager) {
        this.realm = realm;
        this.accountManager = accountManager;

    }

    public ArrayList<Conversation> listAllConversations() {
        RealmResults<Conversation> conversations = realm.where(Conversation.class).findAll();
        return new ArrayList<>(conversations);
    }

    public Conversation getConversation(Long id) {
        Conversation conversation = realm.where(Conversation.class).equalTo("id", id).findFirst();
        if (conversation != null) {
            return conversation;
        } else return null;
    }

    public Conversation addConversation(Contact contact, Session session) {
        if (contact == null) return null;
        realm.beginTransaction();
            Conversation conversation = realm.createObject(Conversation.class, generateConversationId());
            conversation.setContact(contact);
            conversation.setSessions(new RealmList<Session>());
            conversation.setMessages(new RealmList<Message>());
            conversation.setAccount(accountManager.getActiveAccount());
            realm.copyToRealmOrUpdate(conversation);
        realm.commitTransaction();
        return conversation;
    }

    public boolean deleteConversationById(Long id) {
        Conversation conversationToDelete = realm.where(Conversation.class).equalTo("id", id).findFirst();
        if (conversationToDelete != null) {
            conversationToDelete.deleteFromRealm();
            return true;
        } else return false;
    }

    public boolean deleteConversationByContactName(String contactName) {
        Conversation conversationToDelete = realm.where(Conversation.class).equalTo("contact.name", contactName).findFirst();
        if (conversationToDelete != null) {
            conversationToDelete.deleteFromRealm();
            return true;
        } else return false;
    }

    public boolean updateConversationById(Long id, Session session) {
        Conversation conversation = realm.where(Conversation.class).equalTo("id", id).findFirst();
        if (conversation == null) return false;
        realm.beginTransaction();
        RealmList<Session> sessions = conversation.getSessions();
        sessions.add(session);
        realm.copyToRealmOrUpdate(sessions);
        realm.commitTransaction();
        return true;
    }

    public boolean updateConversationByContactName(String name, Session session) {
        Conversation conversation = realm.where(Conversation.class).equalTo("contact.name", name).findFirst();
        if (conversation == null) return false;
        realm.beginTransaction();
        RealmList<Session> sessions = conversation.getSessions();
        sessions.add(session);
        realm.copyToRealmOrUpdate(sessions);
        realm.commitTransaction();
        return true;
    }

    private long generateConversationId() {
        long id;
        try {
            if (realm.where(Conversation.class).max("id") == null) {
                id = 0;
            } else {
                id = realm.where(Conversation.class).max("id").longValue() + 1;
            }
        } catch(ArrayIndexOutOfBoundsException ex) {
            id = 0;
        }
        return id;
    }

}
