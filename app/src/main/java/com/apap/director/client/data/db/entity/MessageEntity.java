package com.apap.director.client.data.db.entity;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageEntity extends RealmObject {

    public MessageEntity() {}

    @PrimaryKey
    private long id;
    private ConversationEntity conversation;
    private String content;
    private Date date;
    private Boolean mine;

    public Boolean isMine() {
        return mine;
    }

    @Override
    public String toString() {
        return content;
    }
}

