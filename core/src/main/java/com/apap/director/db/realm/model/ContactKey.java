package com.apap.director.db.realm.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ContactKey extends RealmObject {

    @PrimaryKey
    private long id;

    private Contact contact;
    private byte[] serialized;
    private int deviceId;
}
