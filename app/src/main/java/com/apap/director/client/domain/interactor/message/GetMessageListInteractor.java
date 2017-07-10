package com.apap.director.client.domain.interactor.message;

import com.apap.director.client.domain.interactor.base.BaseInteractor;
import com.apap.director.client.domain.model.MessageModel;
import com.apap.director.client.domain.repository.MessageRepository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * Created by Adam Poterałowicz
 */

public class GetMessageListInteractor extends BaseInteractor<List<MessageModel>, Long> {

    private MessageRepository messageRepository;

    @Inject
    public GetMessageListInteractor(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    public Observable<List<MessageModel>> buildObservable(Long contactId) {
        return messageRepository.getMessagesByContact(contactId);
    }
}
