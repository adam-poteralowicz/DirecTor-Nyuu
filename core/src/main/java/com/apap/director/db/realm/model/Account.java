package com.apap.director.db.realm.model;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Account extends RealmObject {

    @PrimaryKey
    private long id;
    private String name;
    private String keyPair;
    private int registrationId;

    private RealmList<Contact> contacts;
    private RealmList<OneTimeKey> oneTimeKeys;
    private SignedKey signedKey;
    private RealmList<Session> sessions;
}
