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
public class Conversation extends RealmObject {

    @PrimaryKey
    private long id;
    private Contact contact;
    private RealmList<Message> messages;
    private Session session;
    private Account account;

    @Override
    public String toString() {
        return getContact().toString();
        //return getContact().getName();
    }
}
