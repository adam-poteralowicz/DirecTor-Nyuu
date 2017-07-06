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
public class ContactEntity extends RealmObject {

    @PrimaryKey
    private long id;
    private String name;
    private RealmList<ContactKeyEntity> contactKeys;
    private AccountEntity owner;
    private String image;

    @Override
    public String toString() {
        return name;
    }
}
