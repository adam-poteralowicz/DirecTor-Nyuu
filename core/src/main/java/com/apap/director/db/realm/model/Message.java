package com.apap.director.db.realm.model;

import java.util.Date;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Message extends RealmObject {
    @PrimaryKey
    private long id;
    private String sender;
    private String recipient;
    private String content;
    private Date date;
    private Boolean mine;
}
