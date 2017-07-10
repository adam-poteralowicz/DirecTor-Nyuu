package com.apap.director.client.data.store;

import android.util.Log;

import com.apap.director.client.data.db.entity.ContactEntity;
import com.apap.director.client.data.db.entity.ContactKeyEntity;
import com.apap.director.client.data.db.service.AccountStore;
import com.apap.director.client.data.manager.AccountManager;

import org.whispersystems.libsignal.IdentityKey;
import org.whispersystems.libsignal.IdentityKeyPair;
import org.whispersystems.libsignal.InvalidKeyException;
import org.whispersystems.libsignal.SignalProtocolAddress;
import org.whispersystems.libsignal.state.IdentityKeyStore;

import javax.inject.Inject;

import io.realm.Realm;

public class IdentityKeyStoreImpl implements IdentityKeyStore {

    private AccountStore accountStore;
    private AccountManager accountManager;

    @Inject
    public IdentityKeyStoreImpl(AccountStore accountStore) {
        this.accountStore = accountStore;
    }

    @Override
    public IdentityKeyPair getIdentityKeyPair() {
        try {
            return new IdentityKeyPair(accountStore.getActiveAccount().getKeyPair());
        } catch (InvalidKeyException e) {
            Log.v("HAI/IdentityKeyStore", "Identity key pair not found");
            Log.getStackTraceString(e);
            return null;
        }
    }

    @Override
    public int getLocalRegistrationId() {
        return accountStore.getActiveAccount().getRegistrationId();
    }

    @Override
    public void saveIdentity(SignalProtocolAddress address, IdentityKey identityKey) {

        //TODO: refactor, będzie powodowało błędy, nie mam pomysłu, dlaczego zamknięcie na końcu nie działa :(
        Realm realm = Realm.getDefaultInstance();

        realm.beginTransaction();
        ContactKeyEntity sameName = realm.where(ContactKeyEntity.class).equalTo("keyBase64", address.getName()).equalTo("deviceId", address.getDeviceId()).findFirst();


        if (sameName != null) {
            realm.commitTransaction();
            realm.close();
            return;
        }

        ContactKeyEntity contactKey = realm.createObject(ContactKeyEntity.class);
        contactKey.setDeviceId(address.getDeviceId());
        contactKey.setKeyBase64(address.getName());


        ContactEntity contact = realm.where(ContactEntity.class)
                .equalTo("id", Long.valueOf(address.getName()))
                .findFirst();

        contact.setContactKey(contactKey);

        realm.commitTransaction();
        realm.close();
    }

    @Override
    public boolean isTrustedIdentity(SignalProtocolAddress address, IdentityKey identityKey) {

        Realm realm = Realm.getDefaultInstance();
        ContactKeyEntity contactKey = realm.where(ContactKeyEntity.class)
                .equalTo("keyBase64", address.getName())
                .equalTo("deviceId", address.getDeviceId())
                .findFirst();

        realm.close();
        return contactKey != null;

    }

}
