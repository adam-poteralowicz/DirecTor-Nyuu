package com.apap.director.client.domain.interactor.inbox;

import com.apap.director.client.domain.interactor.base.BaseInteractor;
import com.apap.director.client.domain.interactor.contact.GetOneTimeKeyInteractor;
import com.apap.director.client.domain.interactor.contact.GetSignedKeyInteractor;
import com.apap.director.client.domain.model.ContactModel;
import com.apap.director.client.domain.model.ConversationModel;
import com.apap.director.client.domain.model.SessionModel;
import com.apap.director.client.domain.repository.ConversationRepository;
import com.apap.director.client.domain.util.EncryptionService;

import org.whispersystems.libsignal.SessionBuilder;

import java.util.ArrayList;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * Created by Alicja Michniewicz
 */

public class CreateConversationInteractor extends BaseInteractor<ConversationModel, ContactModel> {

    private ConversationRepository conversationRepository;
    private GetOneTimeKeyInteractor getOneTimeKeyInteractor;
    private GetSignedKeyInteractor getSignedKeyInteractor;
    private EncryptionService encryptionService;

    @Inject
    public CreateConversationInteractor(ConversationRepository conversationRepository, GetOneTimeKeyInteractor getOneTimeKeyInteractor, GetSignedKeyInteractor getSignedKeyInteractor) {
        this.conversationRepository = conversationRepository;
        this.getOneTimeKeyInteractor = getOneTimeKeyInteractor;
        this.getSignedKeyInteractor = getSignedKeyInteractor;
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

    private Observable<SessionModel> buildSession() {
    }
}
