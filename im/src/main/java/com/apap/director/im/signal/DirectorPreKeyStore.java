package com.apap.director.im.signal;

import org.whispersystems.libsignal.InvalidKeyIdException;
import org.whispersystems.libsignal.state.*;
import org.whispersystems.libsignal.state.PreKeyRecord;

/**
 * Created by Ala on 29/11/2016.
 */

public class DirectorPreKeyStore implements PreKeyStore {



    @Override
    public PreKeyRecord loadPreKey(int preKeyId) throws InvalidKeyIdException {
        return null;
    }

    @Override
    public void storePreKey(int preKeyId, PreKeyRecord record) {

    }

    @Override
    public boolean containsPreKey(int preKeyId) {
        return false;
    }

    @Override
    public void removePreKey(int preKeyId) {

    }
}
