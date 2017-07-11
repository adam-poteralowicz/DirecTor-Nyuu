package com.apap.director.client.data.db.entity;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConversationEntity extends RealmObject {

    public ConversationEntity() {}

    @PrimaryKey
    private long id;
    private ContactEntity contact;
    private RealmList<MessageEntity> messages;

}
