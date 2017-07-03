package com.apap.director.client.data.db.entity;

import java.util.Date;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MessageEntity extends RealmObject {

    @PrimaryKey
    private long id;
    private String sender;
    private String recipient;
    private String content;
    private Date date;
    private Boolean mine;
    private AccountEntity account;
    private ConversationEntity conversation;

    public Boolean isMine() {
        return mine;
    }

    @Override
    public String toString() {
        return content;
    }
}

