package com.apap.director.client.domain.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Session extends RealmObject {

    @PrimaryKey
    private long id;
    private Conversation conversation;
    private int deviceId;
    private byte[] serializedKey;
    private Account account;
    private String name;


}
