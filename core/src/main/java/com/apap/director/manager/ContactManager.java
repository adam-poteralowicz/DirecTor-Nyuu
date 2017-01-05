package com.apap.director.manager;

import android.util.Base64;

import com.apap.director.db.realm.model.Contact;
import com.apap.director.db.realm.model.ContactKey;
import com.apap.director.db.realm.model.Conversation;
import com.apap.director.network.rest.service.KeyService;
import com.apap.director.network.rest.service.UserService;

import org.whispersystems.libsignal.util.ByteUtil;

import java.security.SecureRandom;
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
    private KeyService keyService;

    @Inject
    public ContactManager(Realm realm, AccountManager manager, KeyService service) {
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



        Realm localRealm = Realm.getDefaultInstance();


        localRealm.beginTransaction();
            Contact contact = realm.createObject(Contact.class, generateContactId());
            contact.setName(name);

            RealmList<ContactKey> keys = new RealmList<>();
            ContactKey contactKey = realm.createObject(ContactKey.class);
            contactKey.setContact(contact);
            contactKey.setId(generateContactKeyId());
            contactKey.setAccount(accountManager.getActiveAccount());

            byte[] decodedKey = Base64.decode(keyBase64, Base64.NO_WRAP | Base64.URL_SAFE);
            contactKey.setSerialized(decodedKey);
            String splitted = Base64.encodeToString(ByteUtil.split(decodedKey, 1, 32)[1], Base64.NO_WRAP | Base64.URL_SAFE);
            contactKey.setKeyBase64(splitted);

            contactKey.setDeviceId(0);
            keys.add(contactKey);
            contact.setContactKeys(keys);

            localRealm.insertOrUpdate(contact);
        localRealm.commitTransaction();
        return true;
    }

//    private boolean addContactKey(String owner, String keyBase64) {
//        realm.beginTransaction();
//            ContactKey contactKey = realm.createObject(ContactKey.class, generateContactKeyId());
//            contactKey.setContact(realm.where(Contact.class).equalTo("name", name).findFirst());
//            contactKey.setKeyBase64(contact.getOneTimeKey());
//            contactKey.setAccount(accountManager.getActiveAccount());
//            //contactKey.setDeviceId();
//            realm.insertOrUpdate(contactKey);
//        realm.commitTransaction();
//        return true;
//    }

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

    public String generateOneTimeKey() {
        SecureRandom sr = new SecureRandom();
        String alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        String otk = "";
        int x;
        for (int i = 0; i < 32; i++) {
            x = sr.nextInt(alphabet.length()-1);
            otk += alphabet.charAt(x);
        }
        return otk;
    }
}
