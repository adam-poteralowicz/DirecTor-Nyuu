package com.apap.director.client.data.repository;

import com.apap.director.client.data.db.entity.MessageEntity;
import com.apap.director.client.data.db.service.DbMessageService;
import com.apap.director.client.domain.repository.MessageRepository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * Created by Adam on 2017-07-05.
 */

public class MessageRepositoryImpl implements MessageRepository {

    private DbMessageService dbMessageService;

    @Inject
    public MessageRepositoryImpl(DbMessageService dbMessageService) {
        this.dbMessageService = dbMessageService;
    }

    @Override
    public Observable<List<MessageEntity>> getMessagesByContact(Long contactId) {
        return Observable.just(dbMessageService.getMessagesByContact(contactId));
    }
}
