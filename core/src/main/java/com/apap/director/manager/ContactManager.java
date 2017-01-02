package com.apap.director.manager;

import com.apap.director.db.realm.model.Contact;
import com.apap.director.db.realm.model.ContactKey;
import com.apap.director.db.realm.model.Conversation;
import com.apap.director.network.rest.service.UserService;
import java.util.ArrayList;
import javax.inject.Inject;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

public class ContactManager {
    private Realm realm;
    private Contact contact;
    private AccountManager accountManager;
    private UserService userService;

    @Inject
    public ContactManager(Realm realm, AccountManager manager) {
        this.realm = realm;
        this.accountManager = manager;
    }

    public ArrayList<Contact> listAllContacts() {
        RealmResults<Contact> contacts = realm.where(Contact.class).findAll();
        return new ArrayList<>(contacts);
    }

    public Contact getContact(String name) {
        return realm.where(Contact.class).equalTo("name", name).findFirst();
    }

    public boolean addContact(String name, String keyBase64) {
        Contact sameName = realm.where(Contact.class).equalTo("name", name).findFirst();
        if (sameName != null) return false;

        realm.beginTransaction();
            Contact contact = realm.createObject(Contact.class, generateContactId());
            contact.setName(name);
            contact.setOneTimeKey(keyBase64);
            realm.insertOrUpdate(contact);
        realm.commitTransaction();
        addContactKey(name);
        return true;
    }

    private boolean addContactKey(String name) {
        realm.beginTransaction();
            ContactKey contactKey = realm.createObject(ContactKey.class, generateContactKeyId());
            contactKey.setContact(realm.where(Contact.class).equalTo("name", name).findFirst());
            contactKey.setKeyBase64(contact.getOneTimeKey());
            contactKey.setAccount(accountManager.getActiveAccount());
            //contactKey.setDeviceId();
            realm.insertOrUpdate(contactKey);
        realm.commitTransaction();
        return true;
    }

    public boolean deleteContact(String name) {
        Contact contactToDelete = realm.where(Contact.class).equalTo("name", name).findFirst();
        if (contactToDelete == null) return false;

        realm.beginTransaction();
            contactToDelete.deleteFromRealm();
        realm.commitTransaction();
        return true;
    }

    public boolean updateContact(String name, String image, Conversation conversation, ContactKey contactKey) {
        Contact contact = realm.where(Contact.class).equalTo("name", name).findFirst();
        if (contact == null) return false;
        realm.beginTransaction();
            if (image != null) {
                contact.setImage(image);
            }
            if (conversation != null) {
                contact.setConversation(conversation);
            }
            if (contactKey != null) {
                if (!contact.getContactKeys().isEmpty()) {
                    RealmList<ContactKey> contactKeys = contact.getContactKeys();
                    contactKeys.add(contactKey);
                } else contact.setContactKeys(new RealmList<ContactKey>(contactKey));
            }
            realm.insertOrUpdate(contact);
        realm.commitTransaction();
        return true;
    }

    private long generateContactId() {
        long id;
        try {
            if (realm.where(Contact.class).max("id") == null) {
                id = 0;
            } else {
                id = realm.where(Contact.class).max("id").longValue() + 1;
            }
        } catch(ArrayIndexOutOfBoundsException ex) {
            id = 0;
        }
        return id;
    }

    private long generateContactKeyId() {
        long id;
        try {
            if (realm.where(ContactKey.class).max("id") == null) {
                id = 0;
            } else {
                id = realm.where(ContactKey.class).max("id").longValue() + 1;
            }
        } catch(ArrayIndexOutOfBoundsException ex) {
            id = 0;
        }
        return id;
    }
}
