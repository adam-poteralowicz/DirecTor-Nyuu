package com.apap.director.client.domain.repository;

import com.apap.director.client.domain.model.MessageModel;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by Adam on 2017-07-04.
 */

public interface MessageRepository {

    Observable<List<MessageModel>> getMessagesByContact(Long contactId);

    Observable<MessageModel> saveMessage(MessageModel message);
}
