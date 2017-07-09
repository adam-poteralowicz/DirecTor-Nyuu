package com.apap.director.client.domain.interactor.message;

import android.support.v4.util.Pair;

import com.apap.director.client.data.net.to.MessageTO;
import com.apap.director.client.domain.interactor.base.BaseInteractor;
import com.apap.director.client.domain.interactor.contact.CreateContactInteractor;
import com.apap.director.client.domain.interactor.contact.GetContactInteractor;
import com.apap.director.client.domain.interactor.inbox.CreateConversationInteractor;
import com.apap.director.client.domain.model.AccountModel;
import com.apap.director.client.domain.model.ContactModel;
import com.apap.director.client.domain.model.ConversationModel;
import com.apap.director.client.domain.model.MessageModel;
import com.apap.director.client.domain.repository.AccountRepository;

import io.reactivex.Observable;

/**
 * Created by Alicja Michniewicz
 */

public class CreateMessageInteractor extends BaseInteractor<MessageModel, MessageTO> {

    private final static int ELEMENT_COUNT = 1;

    private CreateContactInteractor createContactInteractor;
    private GetContactInteractor getContactInteractor;
    private CreateConversationInteractor createConversationInteractor;
    private GetConversationIn
    private AccountRepository accountRepository;

    @Override
    protected Observable<MessageModel> buildObservable(MessageTO messageTO) {
        return getOrCreateRecipent()
    }

    private Observable<ContactModel> getOrCreateRecipent(AccountModel account, MessageTO messageTO) {
        return getContactInteractor.execute(messageTO.getFrom())
                .concatWith(createContactInteractor.execute(new Pair<>(messageTO.getFrom(), messageTO.getFrom())))
                .take(ELEMENT_COUNT);
    }

    private Observable<ConversationModel> getOrCreateConversation(ContactModel contactModel) {
        return getCon
    }

    private Observable<ContactModel> chooseContactObservable(ContactModel contactModel, MessageTO) {
            if(contactModel == null) {
                return createContactInteractor.execute(new Pair<>(messageTO.getFrom(), messageTO.getFrom()));
            }
            else {
                return Observable.just(contactModel);
            }}
        }
}
