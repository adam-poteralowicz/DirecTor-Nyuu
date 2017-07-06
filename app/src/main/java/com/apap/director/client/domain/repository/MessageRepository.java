package com.apap.director.client.domain.repository;

import com.apap.director.client.data.db.entity.MessageEntity;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by Adam on 2017-07-04.
 */

public interface MessageRepository {

    Observable<List<MessageEntity>> getMessagesByContact(Long contactId);
}
