package com.apap.director.client.domain.interactor.inbox;

import com.apap.director.client.data.db.entity.ConversationEntity;
import com.apap.director.client.domain.interactor.base.BaseInteractor;
import com.apap.director.client.domain.repository.ConversationRepository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * Created by Adam on 2017-07-05.
 */

public class GetConversationListInteractor extends BaseInteractor<List<ConversationEntity>, Void> {

    private ConversationRepository conversationRepository;

    @Inject
    public GetConversationListInteractor(ConversationRepository conversationRepository) {
        this.conversationRepository = conversationRepository;
    }

    @Override
    public Observable<List<ConversationEntity>> buildObservable(Void aVoid) {
        return conversationRepository.getConversationList();
    }
}
