package com.apap.director.im.signal;


import com.apap.director.db.dao.model.DbSignedPreKey;
import com.apap.director.db.manager.DatabaseManager;

import org.whispersystems.curve25519.Curve25519KeyPair;
import org.whispersystems.libsignal.InvalidKeyIdException;
import org.whispersystems.libsignal.ecc.DjbECPublicKey;
import org.whispersystems.libsignal.ecc.ECKeyPair;
import org.whispersystems.libsignal.state.SignedPreKeyRecord;
import org.whispersystems.libsignal.state.SignedPreKeyStore;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;

import javax.inject.Inject;

public class DirectorSignedPreKeyStore implements SignedPreKeyStore {

    @Inject
    DatabaseManager manager;

    @Override
    public SignedPreKeyRecord loadSignedPreKey(int signedPreKeyId) throws InvalidKeyIdException {
        return null;
    }

    @Override
    public List<SignedPreKeyRecord> loadSignedPreKeys() {

        List<DbSignedPreKey> list = manager.listDbSignedPreKeys();
        return null;
    }

    @Override
    public void storeSignedPreKey(int signedPreKeyId, SignedPreKeyRecord record) {

    }

    @Override
    public boolean containsSignedPreKey(int signedPreKeyId) {
        return false;
    }

    @Override
    public void removeSignedPreKey(int signedPreKeyId) {
        //manager.deleteSig

    }


}
