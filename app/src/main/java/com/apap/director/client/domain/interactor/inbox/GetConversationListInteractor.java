package com.apap.director.client.domain.interactor.inbox;

import com.apap.director.client.domain.interactor.base.BaseInteractor;
import com.apap.director.client.domain.model.ConversationModel;
import com.apap.director.client.domain.repository.ConversationRepository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * Created by Adam Poterałowicz
 */

public class GetConversationListInteractor extends BaseInteractor<List<ConversationModel>, Void> {

    private ConversationRepository conversationRepository;

    @Inject
    public GetConversationListInteractor(ConversationRepository conversationRepository) {
        this.conversationRepository = conversationRepository;
    }

    @Override
    public Observable<List<ConversationModel>> buildObservable(Void aVoid) {
        return conversationRepository.getConversationList();
    }
}
