package com.apap.director.db.realm.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Session extends RealmObject {

    @PrimaryKey
    private long id;
    private Conversation conversation;
    private int deviceId;
    private String name;
    private byte[] serializedKey;
    private Account account;
}
