package com.apap.director.client.domain.interactor.message;

import com.apap.director.client.data.db.entity.MessageEntity;
import com.apap.director.client.domain.interactor.base.BaseInteractor;
import com.apap.director.client.domain.repository.MessageRepository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * Created by Adam on 2017-07-04.
 */

public class GetMessageListInteractor extends BaseInteractor<List<MessageEntity>, Void> {

    private MessageRepository messageRepository;
    public Long contactId;

    @Inject
    GetMessageListInteractor(MessageRepository messageRepository, Long contactId) {
        this.messageRepository = messageRepository;
        this.contactId = contactId;
    }

    @Override
    public Observable<List<MessageEntity>> buildObservable(Void aVoid) {
        return messageRepository.getMessagesByContact(this.contactId);
    }
}
