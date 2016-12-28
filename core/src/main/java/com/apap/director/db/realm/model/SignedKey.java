package com.apap.director.db.realm.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class SignedKey extends RealmObject {

    @PrimaryKey
    private long id;
    private int signedKeyId;
    private byte[] serializedKey;
    private String identityName;
}
