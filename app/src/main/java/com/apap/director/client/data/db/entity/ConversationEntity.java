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
public class ConversationEntity extends RealmObject {

    @PrimaryKey
    private long id;
    private AccountEntity owner;
    private ContactEntity interlocutor;
    private RealmList<MessageEntity> messages;
    private RealmList<SessionEntity> sessions;

}
