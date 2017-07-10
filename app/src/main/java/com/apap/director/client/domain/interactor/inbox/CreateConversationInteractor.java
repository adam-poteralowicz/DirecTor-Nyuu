package com.apap.director.client.domain.interactor.inbox;

import com.apap.director.client.data.net.to.OneTimeKeyTO;
import com.apap.director.client.data.net.to.SignedKeyTO;
import com.apap.director.client.domain.interactor.base.BaseInteractor;
import com.apap.director.client.domain.interactor.contact.GetOneTimeKeyInteractor;
import com.apap.director.client.domain.interactor.contact.GetSignedKeyInteractor;
import com.apap.director.client.domain.model.ContactModel;
import com.apap.director.client.domain.model.ConversationModel;
import com.apap.director.client.domain.repository.ConversationRepository;
import com.apap.director.client.domain.util.EncryptionService;

import java.util.ArrayList;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.BiFunction;

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
        return buildSession(contactModel)
                .flatMap(aVoid -> conversationRepository.findLastId())
                .flatMap(id -> createConversationModel(id, contactModel));
    }

    private Observable<ConversationModel> createConversationModel(long id, ContactModel contactModel) {
        ConversationModel conversationModel = new ConversationModel();
        conversationModel.setContact(contactModel);
        conversationModel.setMessages(new ArrayList<>());
        conversationModel.setId(id);

        return Observable.just(conversationModel);
    }

    private Observable<Void> buildSession(ContactModel contactModel) {
        return getSignedKeyInteractor.execute(contactModel)
                .zipWith(getOneTimeKeyInteractor.execute(contactModel), new BiFunction<SignedKeyTO, OneTimeKeyTO, Void>() {
                    @Override
                    public Void apply(@NonNull SignedKeyTO signedKeyTO, @NonNull OneTimeKeyTO oneTimeKeyTO) throws Exception {
                        encryptionService.buildSession(contactModel.getContactKey(), oneTimeKeyTO, signedKeyTO);
                        return null;
                    }
                });
    }

}
