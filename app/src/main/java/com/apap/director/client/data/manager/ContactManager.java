package com.apap.director.client.data.manager;

import android.util.Base64;
import android.util.Log;

import com.apap.director.client.data.db.entity.AccountEntity;
import com.apap.director.client.data.db.entity.ContactEntity;
import com.apap.director.client.data.db.entity.ContactKeyEntity;
import com.apap.director.client.data.db.entity.ConversationEntity;
import com.apap.director.client.data.net.rest.service.KeyService;

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

    public List<ContactEntity> listAllContacts() {
        RealmResults<ContactEntity> contacts = realm.where(ContactEntity.class).findAll();
        return new ArrayList<>(contacts);
    }

    public ContactEntity getContact(String name) {
        return realm.where(ContactEntity.class).equalTo("name", name).findFirst();
    }

    public ContactEntity getContactByContactKey(String keyBase64) {

        ContactKeyEntity contactKey = realm.where(ContactKeyEntity.class).equalTo("keyBase64", keyBase64).findFirst();

        if (contactKey == null) {
            return null;
        }
        return contactKey.getContact();
    }

    public void addContact(String name, String keyBase64) {

        Realm localRealm = Realm.getDefaultInstance();

        ContactEntity sameName = localRealm.where(ContactEntity.class).equalTo("name", name).findFirst();
        if (sameName != null) {

            localRealm.close();
            return;
        }

        localRealm.beginTransaction();
        ContactEntity contact = localRealm.createObject(ContactEntity.class, generateContactId(localRealm));
        contact.setName(name);

        RealmList<ContactKeyEntity> keys = new RealmList<>();
        ContactKeyEntity contactKey = localRealm.createObject(ContactKeyEntity.class, generateContactKeyId(localRealm));
        contactKey.setAccount(localRealm.where(AccountEntity.class).equalTo("active", true).findFirst());

        byte[] decodedKey = Base64.decode(keyBase64, Base64.NO_WRAP | Base64.URL_SAFE);
        contactKey.setSerialized(decodedKey);
        byte[][] typeAndKey = ByteUtil.split(decodedKey, 1, 32);

        String splitted = Base64.encodeToString(typeAndKey[1], Base64.NO_WRAP | Base64.URL_SAFE);
        contactKey.setKeyBase64(splitted);

        contactKey.setDeviceId(0);
        keys.add(contactKey);
        contact.setContactKeys(keys);
        contact.setAccount(localRealm.where(AccountEntity.class).equalTo("active", true).findFirst());

        contactKey.setContact(contact);

        localRealm.commitTransaction();
        localRealm.close();
    }

    public boolean deleteContact(String name) {
        ContactEntity contactToDelete = realm.where(ContactEntity.class).equalTo("name", name).findFirst();
        if (contactToDelete == null)
            return false;

        realm.beginTransaction();
        contactToDelete.getContactKeys().deleteAllFromRealm();

        ConversationEntity conversationToDelete = contactToDelete.getConversation();
        conversationToDelete.getSessions().deleteAllFromRealm();
        conversationToDelete.getMessages().deleteAllFromRealm();
        conversationToDelete.deleteFromRealm();

        contactToDelete.deleteFromRealm();
        realm.commitTransaction();
        return true;
    }

    public boolean updateContact(String name, String image, ConversationEntity conversation, ContactKeyEntity contactKey) {
        ContactEntity contact = realm.where(ContactEntity.class).equalTo("name", name).findFirst();
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
                RealmList<ContactKeyEntity> contactKeys = contact.getContactKeys();
                contactKeys.add(contactKey);
            } else contact.setContactKeys(new RealmList<>(contactKey));
        }
        realm.insertOrUpdate(contact);
        realm.commitTransaction();
        return true;
    }

    private long generateContactId(Realm realm) {
        long id;
        try {
            if (realm.where(ContactEntity.class).max("id") == null) {
                id = 0;
            } else {
                id = realm.where(ContactEntity.class).max("id").longValue() + 1;
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            id = 0;
            Log.getStackTraceString(e);
        }
        return id;
    }

    private long generateContactKeyId(Realm realm) {
        long id;
        try {
            if (realm.where(ContactKeyEntity.class).max("id") == null) {
                id = 0;
            } else {
                id = realm.where(ContactKeyEntity.class).max("id").longValue() + 1;
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            id = 0;
            Log.getStackTraceString(e);
        }
        return id;
    }

    public String generateOneTimeKey() {
        SecureRandom sr = new SecureRandom();
        String alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        String otk = "";
        StringBuilder stringBuilder = new StringBuilder(otk);
        int x;

        for (int i = 0; i < 32; i++) {
            x = sr.nextInt(alphabet.length() - 1);
            stringBuilder.append(Character.toString(alphabet.charAt(x)));
        }
        return stringBuilder.toString();
    }
}
