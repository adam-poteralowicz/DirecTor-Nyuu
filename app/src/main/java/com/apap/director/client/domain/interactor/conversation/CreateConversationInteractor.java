package com.apap.director.client.domain.interactor.conversation;

import com.apap.director.client.domain.interactor.base.BaseInteractor;
import com.apap.director.client.domain.model.ContactModel;
import com.apap.director.client.domain.model.ConversationModel;
import com.apap.director.client.domain.repository.ConversationRepository;

import java.util.ArrayList;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * Created by Alicja Michniewicz
 */

public class CreateConversationInteractor extends BaseInteractor<ConversationModel, ContactModel> {

    private ConversationRepository conversationRepository;

    @Inject
    public CreateConversationInteractor(ConversationRepository conversationRepository) {
        this.conversationRepository = conversationRepository;
    }

    @Override
    protected Observable<ConversationModel> buildObservable(ContactModel contactModel) {
        return conversationRepository.findLastId()
                .flatMap(id -> createConversationModel(id, contactModel));
    }

    private Observable<ConversationModel> createConversationModel(long id, ContactModel contactModel) {
        ConversationModel conversationModel = new ConversationModel();
        conversationModel.setContact(contactModel);
        conversationModel.setMessages(new ArrayList<>());
        conversationModel.setId(id);

        return Observable.just(conversationModel);
    }
}
