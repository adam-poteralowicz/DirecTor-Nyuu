package com.apap.director.client.data.repository;

import com.apap.director.client.data.db.entity.MessageEntity;
import com.apap.director.client.data.db.mapper.MessageMapper;
import com.apap.director.client.data.db.service.DbMessageService;
import com.apap.director.client.domain.model.MessageModel;
import com.apap.director.client.domain.repository.MessageRepository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * Created by Adam on 2017-07-05.
 */

public class MessageRepositoryImpl implements MessageRepository {

    private DbMessageService dbMessageService;
    private MessageMapper messageMapper;

    @Inject
    public MessageRepositoryImpl(DbMessageService dbMessageService, MessageMapper messageMapper) {
        this.dbMessageService = dbMessageService;
        this.messageMapper = messageMapper;
    }

    @Override
    public Observable<List<MessageModel>> getMessagesByContact(Long contactId) {
        return Observable.just(messageMapper.mapToList(messageMapper, dbMessageService.getMessagesByContact(contactId)));
    }

    @Override
    public Observable<MessageModel> saveMessage(MessageModel message) {
        MessageEntity entity = messageMapper.mapToEntity(message);
        dbMessageService.saveMessage(entity);
        return Observable.just(message);
    }
}
