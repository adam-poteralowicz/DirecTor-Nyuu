package com.apap.director.client.domain.interactor.message;

import com.apap.director.client.domain.interactor.base.BaseInteractor;
import com.apap.director.client.domain.model.MessageModel;
import com.apap.director.client.domain.repository.MessageRepository;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * Created by Adam Poterałowicz
 */

public class DeleteMessageInteractor extends BaseInteractor<Boolean, MessageModel> {

    private MessageRepository messageRepository;

    @Inject
    public DeleteMessageInteractor(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    protected Observable<Boolean> buildObservable(MessageModel messageModel) {
        return messageRepository.deleteMessage(messageModel);
    }
}
