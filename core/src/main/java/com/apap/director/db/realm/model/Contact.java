package com.apap.director.db.realm.model;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Contact extends RealmObject {

    @PrimaryKey
    private long id;
    private String name;
    private Conversation conversation;
    private RealmList<ContactKey> contactKeys;

}
