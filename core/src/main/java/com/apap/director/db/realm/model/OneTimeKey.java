package com.apap.director.db.realm.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class OneTimeKey extends RealmObject {

    @PrimaryKey
    private long id;
    private int oneTimeKeyId;
    private byte[] serializedKey;
    private Account account;

}
