package com.apap.director.client.data.db.entity;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountEntity extends RealmObject {

    public AccountEntity() {
        // not called
    }

    @PrimaryKey
    private String keyBase64;
    private String name;
    private byte[] keyPair;
    private int registrationId;
    private RealmList<OneTimeKeyEntity> oneTimeKeys;
    private SignedKeyEntity signedKey;
    private boolean registered;
    private boolean active;
    private String cookie;

    @Override
    public String toString() {
        return name;
    }
}
