package com.apap.director.client.domain.interactor.message;

import com.apap.director.client.data.db.mapper.base.BaseMapper;
import com.apap.director.client.data.net.service.WebSocketService;
import com.apap.director.client.data.net.to.MessageTO;
import com.apap.director.client.domain.interactor.base.BaseInteractor;
import com.apap.director.client.domain.model.AccountModel;
import com.apap.director.client.domain.model.MessageModel;
import com.apap.director.client.domain.repository.ContactRepository;
import com.apap.director.client.domain.repository.MessageRepository;
import com.apap.director.client.domain.util.EncryptionService;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import javax.inject.Inject;

import io.reactivex.Observable;
import ua.naiksoftware.stomp.client.StompMessage;

/**
 * Created by Alicja Michniewicz
 */

public class GetMessageStreamInteractor extends BaseInteractor<MessageModel, AccountModel> {

    private WebSocketService webSocketService;
    private MessageRepository messageRepository;
    private EncryptionService encryptionService;
    private ContactRepository contactRepository;

    @Inject
    public GetMessageStreamInteractor(WebSocketService webSocketService, MessageRepository messageRepository, EncryptionService encryptionService) {
        this.webSocketService = webSocketService;
        this.messageRepository = messageRepository;
        this.encryptionService = encryptionService;
    }

    @Override
    protected Observable<MessageModel> buildObservable(AccountModel account) {

        return webSocketService.getMessages(account.getCookie())
                .map(this::mapToMessageTO)
                .flatMap(messageTO -> )}

    private MessageModel createMessageModel(MessageTO messageTO, AccountModel account) {
        MessageModel messageModel = new MessageModel();
        messageModel.setContent(messageTO.getMessage());

        contactRepository.getContact(account.getKeyBase64(), messageTO.getFrom());
        messageModel.

    }

    private MessageTO mapToMessageTO(StompMessage stompMessage) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(stompMessage.getPayload(), MessageTO.class);
    }
}
