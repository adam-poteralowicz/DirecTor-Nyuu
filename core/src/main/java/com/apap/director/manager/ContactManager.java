package com.apap.director.manager;

import android.util.Base64;

import com.apap.director.db.realm.model.Account;
import com.apap.director.db.realm.model.Contact;
import com.apap.director.db.realm.model.ContactKey;
import com.apap.director.db.realm.model.Conversation;
import com.apap.director.network.rest.service.KeyService;

import org.whispersystems.libsignal.util.ByteUtil;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

public class ContactManager {
    private Realm realm;
    private AccountManager accountManager;

    @Inject
    public ContactManager(Realm realm, AccountManager manager, KeyService service) {
        this.realm = realm;
        this.accountManager = manager;
    }

    public List<Contact> listAllContacts() {
        RealmResults<Contact> contacts = realm.where(Contact.class).findAll();
        return new ArrayList<>(contacts);
    }

    public Contact getContact(String name) {
        return realm.where(Contact.class).equalTo("name", name).findFirst();
    }

    public Contact getContactByContactKey(String keyBase64){

            ContactKey contactKey = realm.where(ContactKey.class).equalTo("keyBase64", keyBase64).findFirst();

            if(contactKey == null) {
                return null;
            }
            return contactKey.getContact();
    }

    public void addContact(String name, String keyBase64) {

        Realm localRealm = Realm.getDefaultInstance();

        Contact sameName = localRealm.where(Contact.class).equalTo("name", name).findFirst();
        if (sameName != null)
            return;

        localRealm.beginTransaction();
            Contact contact = localRealm.createObject(Contact.class, generateContactId(localRealm));
            contact.setName(name);

            RealmList<ContactKey> keys = new RealmList<>();
            ContactKey contactKey = localRealm.createObject(ContactKey.class, generateContactKeyId(localRealm));
            contactKey.setAccount(localRealm.where(Account.class).equalTo("active", true).findFirst());

            byte[] decodedKey = Base64.decode(keyBase64, Base64.NO_WRAP | Base64.URL_SAFE);
            contactKey.setSerialized(decodedKey);
            byte[][] typeAndKey = ByteUtil.split(decodedKey, 1, 32);

            String splitted = Base64.encodeToString(typeAndKey[1], Base64.NO_WRAP | Base64.URL_SAFE);
            contactKey.setKeyBase64(splitted);

            contactKey.setDeviceId(0);
            keys.add(contactKey);
            contact.setContactKeys(keys);
            contact.setAccount(localRealm.where(Account.class).equalTo("active", true).findFirst());

            contactKey.setContact(contact);

        localRealm.commitTransaction();
        localRealm.close();
    }

    public boolean deleteContact(String name) {
        Contact contactToDelete = realm.where(Contact.class).equalTo("name", name).findFirst();
        if (contactToDelete == null)
            return false;

        realm.beginTransaction();
            contactToDelete.getContactKeys().deleteAllFromRealm();

            Conversation conversationToDelete = contactToDelete.getConversation();
            conversationToDelete.getSessions().deleteAllFromRealm();
            conversationToDelete.getMessages().deleteAllFromRealm();
            conversationToDelete.deleteFromRealm();

            contactToDelete.deleteFromRealm();
        realm.commitTransaction();
        return true;
    }

    public boolean updateContact(String name, String image, Conversation conversation, ContactKey contactKey) {
        Contact contact = realm.where(Contact.class).equalTo("name", name).findFirst();
        if (contact == null)
            return false;
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

    private long generateContactId(Realm realm) {
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

    private long generateContactKeyId(Realm realm) {
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

    public String generateOneTimeKey() {
        SecureRandom sr = new SecureRandom();
        String alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        String otk = "";
        int x;
        for (int i = 0; i < 32; i++) {
            x = sr.nextInt(alphabet.length()-1);
            otk += Character.toString(alphabet.charAt(x));
        }
        return otk;
    }
}
