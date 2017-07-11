package com.apap.director.client.data.db.entity;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignedKeyEntity extends RealmObject {

    public SignedKeyEntity() {}

    @PrimaryKey
    private long id;
    private int signedKeyId;
    private byte[] serializedKey;
}
