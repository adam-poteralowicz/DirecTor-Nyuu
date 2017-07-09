package com.apap.director.client.domain.interactor.message;

import com.apap.director.client.domain.interactor.base.BaseInteractor;
import com.apap.director.client.domain.model.MessageModel;
import com.apap.director.client.domain.repository.MessageRepository;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * Created by Adam Poterałowicz
 */

public class SaveMessageInteractor extends BaseInteractor<MessageModel, MessageModel> {

    private MessageRepository messageRepository;

    @Inject
    public SaveMessageInteractor(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    protected Observable<MessageModel> buildObservable(MessageModel messageModel) {
        return messageRepository.saveMessage(messageModel);
    }
}
