package com.apap.director.client.data.db.entity;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SessionEntity extends RealmObject {

    public SessionEntity() {}

    @PrimaryKey
    private long id;
    private int deviceId;
    private byte[] serializedKey;
    private String name;
    private AccountEntity owner;


}
