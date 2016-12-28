package com.apap.director.db.realm.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


public class ContactKey extends RealmObject {

    @PrimaryKey
    private long id;

    private Contact contact;
    private byte[] serialized;
    private int deviceId;
}
