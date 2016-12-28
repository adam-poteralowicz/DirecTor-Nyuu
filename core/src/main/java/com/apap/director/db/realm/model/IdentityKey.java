package com.apap.director.db.realm.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class IdentityKey extends RealmObject {
    @PrimaryKey
    private long id;
    private int deviceId;
    private String name;
    private byte[] key;
    private String identityName;
}
