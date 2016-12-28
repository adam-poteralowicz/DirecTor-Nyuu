package com.apap.director.db.realm.model;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Contact extends RealmObject {

    @PrimaryKey
    private long id;
    private String name;
    private Conversation conversation;
    private RealmList<ContactKey> contactKeys;
    private String image;

    @Override
    public String toString() {
        return name;
    }
}
