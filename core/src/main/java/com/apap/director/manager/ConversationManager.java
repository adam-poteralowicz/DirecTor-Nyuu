package com.apap.director.manager;

import com.apap.director.db.realm.model.Contact;
import com.apap.director.db.realm.model.Conversation;
import com.apap.director.db.realm.model.Message;
import com.apap.director.db.realm.model.Session;

import java.util.ArrayList;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

public class ConversationManager {
    private Realm realm;
    private AccountManager accountManager;

    @Inject
    public ConversationManager(Realm realm, AccountManager accountManager) {
        this.realm = realm;
        this.accountManager = accountManager;

    }

    public ArrayList<Conversation> listAllConversations() {
        RealmResults<Conversation> conversations = realm.where(Conversation.class).findAll();
        return new ArrayList<>(conversations);
    }

    public Conversation getConversationById(Long id) {
        Conversation conversation = realm.where(Conversation.class).equalTo("id", id).findFirst();
        if (conversation != null) {
            return conversation;
        } else return null;
    }

    public Conversation getConversationByContactName(String contactName) {
        Conversation conversation = realm.where(Conversation.class).equalTo("contact.name", contactName).findFirst();
        if (conversation != null) {
            return conversation;
        } else return null;
    }

    public Conversation getConversationByContactId(Long contactId) {
        Realm realm = Realm.getDefaultInstance();
        Conversation conversation = realm.where(Conversation.class).equalTo("contact.id", contactId).findFirst();
        realm.close();
        return conversation;
    }

    public Conversation getConversationByAccountId(Long accountId) {
        return realm.where(Conversation.class).equalTo("account.id", accountId).findFirst();
    }

    public Conversation addConversation(Contact contact) {
        if (contact == null) return null;

        Realm realm = Realm.getDefaultInstance();

        realm.beginTransaction();
            Contact managedContact = realm.copyToRealmOrUpdate(contact);
            Conversation conversation = realm.createObject(Conversation.class, generateConversationId(realm));
            conversation.setContact(contact);
            RealmList<Session> sessions =  new RealmList<Session>();
            conversation.setSessions(sessions);
            conversation.setMessages(new RealmList<Message>());
            conversation.setAccount(accountManager.getActiveAccount());

            managedContact.setConversation(conversation);
            realm.copyToRealmOrUpdate(conversation);
        realm.commitTransaction();

        realm.close();
        return conversation;
    }

    public boolean deleteConversationById(Long id) {
        Conversation conversationToDelete = realm.where(Conversation.class).equalTo("id", id).findFirst();
        if (conversationToDelete != null) {
            realm.beginTransaction();
                conversationToDelete.deleteFromRealm();
            realm.commitTransaction();
            return true;
        } else return false;
    }

    public boolean deleteConversationByContactName(String contactName) {
        Conversation conversationToDelete = realm.where(Conversation.class).equalTo("contact.name", contactName).findFirst();
        if (conversationToDelete != null) {
            realm.beginTransaction();
                conversationToDelete.deleteFromRealm();
            realm.commitTransaction();
            return true;
        } else return false;
    }

    public boolean deleteConversationByContactId(Long contactId) {
        Conversation conversationToDelete = realm.where(Conversation.class).equalTo("contact.id", contactId).findFirst();
        if (conversationToDelete != null) {
            realm.beginTransaction();
                conversationToDelete.deleteFromRealm();
            realm.commitTransaction();
            return true;
        } else return false;
    }

    public boolean deleteConversationByAccountId(Long accountId) {
        Conversation conversationToDelete = realm.where(Conversation.class).equalTo("account.id", accountId).findFirst();
        if (conversationToDelete != null) {
            realm.beginTransaction();
                conversationToDelete.deleteFromRealm();
            realm.commitTransaction();
            return true;
        } else return false;
    }

    public boolean updateConversationSessionsById(Long id, Session session) {
        Conversation conversation = realm.where(Conversation.class).equalTo("id", id).findFirst();
        if (conversation == null) return false;
        realm.beginTransaction();
            RealmList<Session> sessions = conversation.getSessions();
            sessions.add(session);
            realm.copyToRealmOrUpdate(sessions);
        realm.commitTransaction();
        return true;
    }

    public boolean updateConversationSessionsByContactName(String name, Session session) {
        Conversation conversation = realm.where(Conversation.class).equalTo("contact.name", name).findFirst();
        if (conversation == null) return false;
        realm.beginTransaction();
            RealmList<Session> sessions = conversation.getSessions();
            sessions.add(session);
            realm.copyToRealmOrUpdate(sessions);
        realm.commitTransaction();
        return true;
    }

    public boolean updateConversationSessionsByContactId(Long contactId, Session session) {
        Conversation conversation = realm.where(Conversation.class).equalTo("contact.id", contactId).findFirst();
        if (conversation == null) return false;
        realm.beginTransaction();
            RealmList<Session> sessions = conversation.getSessions();
            sessions.add(session);
            realm.copyToRealmOrUpdate(sessions);
        realm.commitTransaction();
        return true;
    }

    public long generateConversationId(Realm realm) {
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
