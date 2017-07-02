package com.apap.director.client.data.store;

import android.util.Log;

import com.apap.director.client.data.manager.AccountManager;
import com.apap.director.client.domain.model.Contact;
import com.apap.director.client.domain.model.ContactKey;

import org.whispersystems.libsignal.IdentityKey;
import org.whispersystems.libsignal.IdentityKeyPair;
import org.whispersystems.libsignal.InvalidKeyException;
import org.whispersystems.libsignal.SignalProtocolAddress;
import org.whispersystems.libsignal.state.IdentityKeyStore;

import javax.inject.Inject;

import io.realm.Realm;

public class DirectorIdentityKeyStore implements IdentityKeyStore {

    private AccountManager accountManager;

    @Inject
    public DirectorIdentityKeyStore(Realm realm, AccountManager accountManager) {
        this.accountManager = accountManager;
    }

    @Override
    public IdentityKeyPair getIdentityKeyPair() {
        try {
            return new IdentityKeyPair(accountManager.getActiveAccount().getKeyPair());
        } catch (InvalidKeyException e) {
            Log.v("HAI/IdentityKeyStore", "Identity key pair not found");
            Log.getStackTraceString(e);
            return null;
        }
    }

    @Override
    public int getLocalRegistrationId() {
        return accountManager.getActiveAccount().getRegistrationId();
    }

    @Override
    public void saveIdentity(SignalProtocolAddress address, IdentityKey identityKey) {

        //TODO: refactor, będzie powodowało błędy, nie mam pomysłu, dlaczego zamknięcie na końcu nie działa :(
        Realm realm = Realm.getDefaultInstance();

        realm.close();

        realm.beginTransaction();
            ContactKey sameName = realm.where(ContactKey.class).equalTo("keyBase64", address.getName()).equalTo("deviceId", address.getDeviceId()).findFirst();


            if (sameName != null) {
                realm.commitTransaction();
                realm.close();
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
        realm.close();
    }

    @Override
    public boolean isTrustedIdentity(SignalProtocolAddress address, IdentityKey identityKey) {

        Realm realm = Realm.getDefaultInstance();
        ContactKey contactKey = realm.where(ContactKey.class)
                .equalTo("keyBase64", address.getName())
                .equalTo("deviceId", address.getDeviceId())
                .findFirst();

        realm.close();
        return contactKey != null;

    }

}
