package com.apap.director.client.domain.interactor.inbox;

import com.apap.director.client.domain.interactor.base.BaseInteractor;
import com.apap.director.client.domain.model.ConversationModel;
import com.apap.director.client.domain.repository.ConversationRepository;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * Created by Adam Poterałowicz
 */

public class DeleteConversationInteractor extends BaseInteractor<Boolean, ConversationModel> {

    private ConversationRepository conversationRepository;

    @Inject
    public DeleteConversationInteractor(ConversationRepository conversationRepository) {
        this.conversationRepository = conversationRepository;
    }

    @Override
    protected Observable<Boolean> buildObservable(ConversationModel conversationModel) {
        return conversationRepository.deleteConversation(conversationModel);
    }
}
