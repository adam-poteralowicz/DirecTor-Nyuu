package com.apap.director.client.data.db.entity;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AccountEntity extends RealmObject {

    @PrimaryKey
    private long id;
    private String name;
    private byte[] keyPair;
    private int registrationId;
    private String masterPassword;

    private String keyBase64;

    private RealmList<ContactEntity> contacts;
    private RealmList<OneTimeKeyEntity> oneTimeKeys;
    private SignedKeyEntity signedKey;
    private RealmList<SessionEntity> sessions;
    private boolean registered;
    private boolean active;
    private String cookie;

    @Override
    public String toString() {
        return name;
    }
}
