package com.apap.director.client.data.db.service;

import com.apap.director.client.data.db.entity.SignedKeyEntity;

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
