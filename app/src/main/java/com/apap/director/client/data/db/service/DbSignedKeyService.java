package com.apap.director.client.data.db.service;

import android.util.Log;

import com.apap.director.client.data.db.entity.AccountEntity;
import com.apap.director.client.data.db.entity.ContactKeyEntity;
import com.apap.director.client.data.db.entity.SignedKeyEntity;
import com.apap.director.client.domain.model.AccountModel;

import org.whispersystems.libsignal.IdentityKeyPair;
import org.whispersystems.libsignal.InvalidKeyIdException;
import org.whispersystems.libsignal.state.SignedPreKeyRecord;
import org.whispersystems.libsignal.state.SignedPreKeyStore;
import org.whispersystems.libsignal.util.KeyHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.realm.Realm;

/**
 * Created by Alicja Michniewicz
 */

public class DbSignedKeyService {

    private final String ID_COLUMN = "id";

    private Realm realm;

    @Inject
    public DbSignedKeyService(Realm realm) {
        this.realm = realm;
    }

    public long findNextId() {
        Number lastId = realm.where(SignedKeyEntity.class).max(ID_COLUMN).longValue();

        if (lastId == null) {
            return 0;
        } else {
            return lastId.longValue() + 1;
        }
    }

}
