package com.apap.director.client.domain.interactor.inbox;

import com.apap.director.client.domain.interactor.base.BaseInteractor;
import com.apap.director.client.domain.model.ContactModel;
import com.apap.director.client.domain.model.ConversationModel;
import com.apap.director.client.domain.repository.AccountRepository;
import com.apap.director.client.domain.repository.ConversationRepository;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * Created by Alicja Michniewicz
 */

public class GetConversationInteractor extends BaseInteractor<ConversationModel, ContactModel> {

    private ConversationRepository conversationRepository;

    @Inject
    public GetConversationInteractor(ConversationRepository conversationRepository) {
        this.conversationRepository = conversationRepository;
    }

    @Override
    protected Observable<ConversationModel> buildObservable(ContactModel contactModel) {
        return conversationRepository.getConversation(contactModel.getId());
    }
}
