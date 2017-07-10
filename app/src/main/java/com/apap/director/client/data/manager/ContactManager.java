package com.apap.director.client.data.manager;

import android.util.Base64;

import com.apap.director.client.data.db.entity.ContactEntity;
import com.apap.director.client.data.db.entity.ContactKeyEntity;
import com.apap.director.client.data.db.entity.ConversationEntity;

import org.whispersystems.libsignal.util.ByteUtil;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

public class ContactManager {
    private Realm realm;

    @Inject
    public ContactManager(Realm realm) {
        this.realm = realm;
    }

    public List<ContactEntity> listAllContacts() {
        RealmResults<ContactEntity> contacts = realm.where(ContactEntity.class).findAll();
        return new ArrayList<>(contacts);
    }

    public ContactEntity getContact(String name) {
        return realm.where(ContactEntity.class).equalTo("name", name).findFirst();
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
        ContactKeyEntity contactKey = localRealm.createObject(ContactKeyEntity.class, generateContactKeyId());

        byte[] decodedKey = Base64.decode(keyBase64, Base64.NO_WRAP | Base64.URL_SAFE);
        contactKey.setSerialized(decodedKey);
        byte[][] typeAndKey = ByteUtil.split(decodedKey, 1, 32);

        String splitted = Base64.encodeToString(typeAndKey[1], Base64.NO_WRAP | Base64.URL_SAFE);
        contactKey.setKeyBase64(splitted);

        contactKey.setDeviceId(0);
        keys.add(contactKey);
        contact.setContactKey(keys.first());

        localRealm.commitTransaction();
        localRealm.close();
    }

    public boolean deleteContact(String name) {
        ContactEntity contactToDelete = realm.where(ContactEntity.class).equalTo("name", name).findFirst();
        if (contactToDelete == null)
            return false;

        realm.beginTransaction();
        contactToDelete.getContactKey().deleteFromRealm();
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
        if (contactKey != null) {
           contact.setContactKey(contactKey);
        }
        realm.insertOrUpdate(contact);
        realm.commitTransaction();
        return true;
    }

    private long generateContactId(Realm realm) {
        Number lastestId = realm.where(ContactEntity.class).max("id").longValue();

        if (lastestId == null) {
            return 0;
        } else {
            return lastestId.longValue() + 1;
        }
    }

    private long generateContactKeyId() {
        Number lastestId = realm.where(ContactKeyEntity.class).max("id").longValue();

        if (lastestId == null) {
            return 0;
        } else {
            return lastestId.longValue() + 1;
        }
    }

}
