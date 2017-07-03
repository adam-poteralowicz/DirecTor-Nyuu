package com.apap.director.client.data.db.entity;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SessionEntity extends RealmObject {

    @PrimaryKey
    private long id;
    private ConversationEntity conversation;
    private int deviceId;
    private byte[] serializedKey;
    private AccountEntity account;
    private String name;


}
