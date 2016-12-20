package com.apap.director.im.signal;

import android.content.Context;

import com.apap.director.db.dao.model.DbIdentityKey;
import com.apap.director.db.manager.DatabaseManager;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.whispersystems.libsignal.IdentityKey;
import org.whispersystems.libsignal.IdentityKeyPair;
import org.whispersystems.libsignal.SignalProtocolAddress;
import org.whispersystems.libsignal.state.IdentityKeyStore;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

public class DirectorIdentityKeyStore implements IdentityKeyStore {

    @Inject
    public DatabaseManager manager;

    private Context context;

    public static final String IDENTITY_PREF = "com.apap.director.identity.pref";
    public static final String KEY_PAIR = "key_pair";
    public static final String LOCAL_ID = "local_id";

    public DirectorIdentityKeyStore(Context context){
        this.context = context;
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
        DbIdentityKey dbKey = new DbIdentityKey();
        dbKey.setName(address.getName());
        dbKey.setDeviceId(address.getDeviceId());
        manager.insertOrUpdateDbIdentityKey(dbKey);
    }

    @Override
    public boolean isTrustedIdentity(SignalProtocolAddress address, IdentityKey identityKey) {

        List<DbIdentityKey> list = manager.listDbIdentityKeysByName(address.getName());

        for(DbIdentityKey key : list) {
            if (key.getDeviceId() == address.getDeviceId()) return true;
        }

        return false;
    }

}
