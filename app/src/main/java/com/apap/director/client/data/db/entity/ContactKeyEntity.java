package com.apap.director.client.data.db.entity;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContactKeyEntity extends RealmObject {

    public ContactKeyEntity() {}

    @PrimaryKey
    private long id;

    private byte[] serialized;
    private int deviceId;
    private String keyBase64;
}
