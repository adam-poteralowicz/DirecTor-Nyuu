package com.apap.director.client.domain.interactor.message;

import com.apap.director.client.data.db.mapper.base.BaseMapper;
import com.apap.director.client.data.net.service.WebSocketService;
import com.apap.director.client.data.net.to.MessageTO;
import com.apap.director.client.domain.interactor.base.BaseInteractor;
import com.apap.director.client.domain.model.MessageModel;
import com.apap.director.client.domain.repository.MessageRepository;
import com.apap.director.client.domain.util.EncryptionService;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * Created by Alicja Michniewicz
 */

public class GetMessageStreamInteractor extends BaseInteractor<MessageModel, String> {

    private WebSocketService webSocketService;
    private MessageRepository messageRepository;
    private EncryptionService encryptionService;

    @Inject
    public GetMessageStreamInteractor(WebSocketService webSocketService, MessageRepository messageRepository, EncryptionService encryptionService) {
        this.webSocketService = webSocketService;
        this.messageRepository = messageRepository;
        this.encryptionService = encryptionService;
    }

    @Override
    protected Observable<MessageModel> buildObservable(String cookie) {
        return webSocketService.getMessages(cookie)
                .map(stompMessage -> {
                    ObjectMapper mapper = new ObjectMapper();
                    MessageTO messageTO = mapper.readValue(stompMessage.getPayload(), MessageTO.class);


                });}

    private MessageModel createMessageModel(MessageTO messageTO) {
        MessageModel messageModel = new MessageModel();
        messageModel.setContent(messageTO.getMessage());


    }
}
