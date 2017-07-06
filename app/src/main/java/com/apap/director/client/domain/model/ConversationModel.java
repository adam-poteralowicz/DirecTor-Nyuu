package com.apap.director.client.domain.model;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by Adam Poterałowicz
 */

@Getter
@Setter
@NoArgsConstructor
public class ConversationModel {

    private long id;
    private AccountModel owner;
    private ContactModel interlocutor;
    private List<MessageModel> messages;
    private List<SessionModel> sessions;
}
