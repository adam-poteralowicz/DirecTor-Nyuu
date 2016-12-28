package com.apap.director.db.realm.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Key extends RealmObject {

    @PrimaryKey
    private long id;
    private int keyId;
    private byte[] serializedKey;
    private String identityName;
}
