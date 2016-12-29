package com.apap.director.im.signal;

import android.content.Context;

import com.apap.director.db.realm.model.ContactKey;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.whispersystems.libsignal.IdentityKey;
import org.whispersystems.libsignal.IdentityKeyPair;
import org.whispersystems.libsignal.SignalProtocolAddress;
import org.whispersystems.libsignal.state.IdentityKeyStore;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmResults;

public class DirectorIdentityKeyStore implements IdentityKeyStore {

    private Context context;
    private Realm realm;

    public static final String IDENTITY_PREF = "com.apap.director.identity.pref";
    public static final String KEY_PAIR = "key_pair";
    public static final String LOCAL_ID = "local_id";

    @Inject
    public DirectorIdentityKeyStore(Context context, Realm realm){
        this.context = context;
        this.realm = realm;
    }

    @Override
    public IdentityKeyPair getIdentityKeyPair() {

        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(context.getSharedPreferences(IDENTITY_PREF, Context.MODE_PRIVATE).getString(KEY_PAIR,null), IdentityKeyPair.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public int getLocalRegistrationId() {
        return context.getSharedPreferences(IDENTITY_PREF, 0).getInt(LOCAL_ID, -1);
    }

    @Override
    public void saveIdentity(SignalProtocolAddress address, IdentityKey identityKey) {
        realm.beginTransaction();
            ContactKey contactKey = realm.createObject(ContactKey.class);
            contactKey.setDeviceId(address.getDeviceId());
            contactKey.setKeyBase64(address.getName());
        realm.commitTransaction();
    }

    @Override
    public boolean isTrustedIdentity(SignalProtocolAddress address, IdentityKey identityKey) {

        List<ContactKey> list = realm.where(ContactKey.class).equalTo("keyBase64", address.getName()).findAll();

        for(ContactKey key : list) {
            if (key.getDeviceId() == address.getDeviceId()) return true;
        }

        return false;
    }

}
