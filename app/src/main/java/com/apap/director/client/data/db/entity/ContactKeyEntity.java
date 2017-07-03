package com.apap.director.client.data.db.entity;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ContactKeyEntity extends RealmObject {

    @PrimaryKey
    private long id;

    private AccountEntity account;
    private ContactEntity contact;
    private byte[] serialized;
    private int deviceId;
    private String keyBase64;
}
