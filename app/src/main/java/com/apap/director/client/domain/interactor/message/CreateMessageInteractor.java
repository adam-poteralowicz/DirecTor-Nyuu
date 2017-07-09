package com.apap.director.client.domain.interactor.message;

import android.support.v4.util.Pair;

import com.apap.director.client.data.net.to.MessageTO;
import com.apap.director.client.domain.interactor.base.BaseInteractor;
import com.apap.director.client.domain.interactor.contact.CreateContactInteractor;
import com.apap.director.client.domain.interactor.contact.GetContactInteractor;
import com.apap.director.client.domain.interactor.inbox.CreateConversationInteractor;
import com.apap.director.client.domain.interactor.inbox.GetConversationInteractor;
import com.apap.director.client.domain.model.AccountModel;
import com.apap.director.client.domain.model.ContactModel;
import com.apap.director.client.domain.model.ConversationModel;
import com.apap.director.client.domain.model.MessageModel;
import com.apap.director.client.domain.repository.AccountRepository;
import com.apap.director.client.domain.repository.ContactRepository;
import com.apap.director.client.domain.repository.MessageRepository;

import java.util.Date;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * Created by Alicja Michniewicz
 */

public class CreateMessageInteractor extends BaseInteractor<MessageModel, MessageTO> {

    private static final int SINGLE_ELEMENT_COUNT = 1;

    private GetContactInteractor getContactInteractor;
    private CreateConversationInteractor createConversationInteractor;
    private GetConversationInteractor getConversationInteractor;
    private MessageRepository messageRepository;

    @Inject


    @Override
    protected Observable<MessageModel> buildObservable(MessageTO messageTO) {
        return getContactInteractor.execute(messageTO.getFrom())
                .flatMap(this::getOrCreateConversation)
                .flatMap(conversation -> createMessage(conversation, messageTO));
    }

    private Observable<ConversationModel> getOrCreateConversation(ContactModel contactModel) {
        return getConversationInteractor.execute(contactModel)
                .concatWith(createConversationInteractor.execute(contactModel))
                .take(SINGLE_ELEMENT_COUNT);
    }

    private Observable<MessageModel> createMessage(ConversationModel conversationModel, MessageTO messageTO) {
        return messageRepository.findNextId()
                .map(id -> {
                    MessageModel messageModel = new MessageModel();
                    messageModel.setContent(messageTO.getMessage());
                    messageModel.setDate(new Date());
                    messageModel.setId(id);
                    messageModel.setConversation(conversationModel);

                    return messageModel;
                });
    }
}
