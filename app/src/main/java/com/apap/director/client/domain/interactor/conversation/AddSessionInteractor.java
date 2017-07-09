package com.apap.director.client.domain.interactor.conversation;

import android.support.v4.util.Pair;

import com.apap.director.client.domain.interactor.base.BaseInteractor;
import com.apap.director.client.domain.model.ConversationModel;
import com.apap.director.client.domain.model.SessionModel;
import com.apap.director.client.domain.repository.ConversationRepository;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * Created by Adam Poterałowicz
 */

public class AddSessionInteractor extends BaseInteractor<ConversationModel, Pair<ConversationModel, SessionModel>> {

    private ConversationRepository conversationRepository;

    @Inject
    public AddSessionInteractor(ConversationRepository conversationRepository) {
        this.conversationRepository = conversationRepository;
    }

    @Override
    protected Observable<ConversationModel> buildObservable(Pair<ConversationModel, SessionModel> pair) {
        ConversationModel conversation = pair.first;
        SessionModel session = pair.second;
        return conversationRepository.addSession(conversation, session);
    }
}
