package com.apap.director.client.domain.model;

import java.util.Date;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by Adam Poterałowicz
 */

@Getter
@Setter
@NoArgsConstructor
public class MessageModel {

    private long id;
    private ConversationModel conversation;
    private String content;
    private Date date;
    private Boolean mine;
}
