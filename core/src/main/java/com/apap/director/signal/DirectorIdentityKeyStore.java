package com.apap.director.signal;

import android.util.Log;

import com.apap.director.db.realm.model.Contact;
import com.apap.director.db.realm.model.ContactKey;
import com.apap.director.manager.AccountManager;

import org.whispersystems.libsignal.IdentityKey;
import org.whispersystems.libsignal.IdentityKeyPair;
import org.whispersystems.libsignal.InvalidKeyException;
import org.whispersystems.libsignal.SignalProtocolAddress;
import org.whispersystems.libsignal.state.IdentityKeyStore;

import javax.inject.Inject;

import io.realm.Realm;

public class DirectorIdentityKeyStore implements IdentityKeyStore {

    private Realm realm;
    private AccountManager accountManager;

    @Inject
    public DirectorIdentityKeyStore(Realm realm, AccountManager accountManager){
        this.realm = realm;
        this.accountManager = accountManager;
    }

    @Override
    public IdentityKeyPair getIdentityKeyPair() {
        try {
            return new IdentityKeyPair(accountManager.getActiveAccount().getKeyPair());
        } catch (InvalidKeyException e) {
            Log.v("HAI/IdentityKeyStore", "Identity key pair not found");
            return null;
        }
    }

    @Override
    public int getLocalRegistrationId() {
        return accountManager.getActiveAccount().getRegistrationId();
    }

    @Override
    public void saveIdentity(SignalProtocolAddress address, IdentityKey identityKey) {

        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
            ContactKey sameName = realm.where(ContactKey.class).equalTo("keyBase64", address.getName()).equalTo("deviceId", address.getDeviceId()).findFirst();

            if(sameName != null) {
                realm.commitTransaction();
                return;
            }

            ContactKey contactKey = realm.createObject(ContactKey.class);
            contactKey.setDeviceId(address.getDeviceId());
            contactKey.setKeyBase64(address.getName());

            Contact contact = realm.where(Contact.class)
                                .equalTo("id", Long.valueOf(address.getName()))
                                .findFirst();

            contact.getContactKeys().add(contactKey);
            contactKey.setContact(contact);

        realm.commitTransaction();
    }

    @Override
    public boolean isTrustedIdentity(SignalProtocolAddress address, IdentityKey identityKey) {

        Realm realm = Realm.getDefaultInstance();
        ContactKey contactKey = realm.where(ContactKey.class)
                .equalTo("keyBase64", address.getName())
                .equalTo("deviceId", address.getDeviceId())
                .findFirst();

        return contactKey == null ? false : true;

    }

}
