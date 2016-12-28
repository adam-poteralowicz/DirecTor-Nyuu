package com.apap.director.db.realm.model;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Ala on 28/12/2016.
 */

public class Conversation extends RealmObject {

    @PrimaryKey
    private long id;
    private Contact contact;
    private RealmList<Message> messages;
    private Session session;
    private String recipient;
}
