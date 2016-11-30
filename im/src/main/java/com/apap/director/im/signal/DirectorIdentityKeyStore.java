package com.apap.director.im.signal;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.whispersystems.libsignal.IdentityKey;
import org.whispersystems.libsignal.IdentityKeyPair;
import org.whispersystems.libsignal.SignalProtocolAddress;
import org.whispersystems.libsignal.state.IdentityKeyStore;

import java.io.IOException;

public class DirectorIdentityKeyStore implements IdentityKeyStore {

    private Context context;


    public static String IDENTITY_PREF = "com.apap.director.identity.pref";
    public static String KEY_PAIR = "key_pair";
    public static String LOCAL_ID = "local_id";

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


    }

    @Override
    public boolean isTrustedIdentity(SignalProtocolAddress address, IdentityKey identityKey) {

        return false;
    }

}
