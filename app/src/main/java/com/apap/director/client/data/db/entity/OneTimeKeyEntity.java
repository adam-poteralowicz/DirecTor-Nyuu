package com.apap.director.client.data.db.entity;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OneTimeKeyEntity extends RealmObject {

    public OneTimeKeyEntity() {}

    @PrimaryKey
    private long id;
    private int oneTimeKeyId;
    private byte[] serializedKey;

}
