package com.apap.director.client.data.db.entity;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OneTimeKeyEntity extends RealmObject {

    @PrimaryKey
    private long id;
    private int oneTimeKeyId;
    private byte[] serializedKey;
    private AccountEntity account;

}
