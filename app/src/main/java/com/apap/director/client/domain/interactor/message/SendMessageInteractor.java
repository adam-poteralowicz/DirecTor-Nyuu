package com.apap.director.client.domain.interactor.message;

import com.apap.director.client.data.net.service.WebSocketService;
import com.apap.director.client.data.net.to.MessageTO;
import com.apap.director.client.domain.interactor.base.BaseInteractor;
import com.apap.director.client.domain.interactor.contact.GetOneTimeKeyInteractor;
import com.apap.director.client.domain.model.AccountModel;
import com.apap.director.client.domain.model.ContactKeyModel;
import com.apap.director.client.domain.model.MessageModel;
import com.apap.director.client.domain.repository.AccountRepository;
import com.apap.director.client.domain.util.EncryptionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.UnsupportedEncodingException;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * Created by Alicja Michniewicz
 */

public class SendMessageInteractor extends BaseInteractor<Void,MessageModel> {

    private WebSocketService webSocketService;
    private EncryptionService encryptionService;
    private AccountRepository accountRepository;

    @Inject
    public SendMessageInteractor(WebSocketService webSocketService, EncryptionService encryptionService, AccountRepository accountRepository) {
        this.webSocketService = webSocketService;
        this.encryptionService = encryptionService;
        this.accountRepository = accountRepository;
    }


    @Override
    protected Observable<Void> buildObservable(MessageModel messageModel) {
        return accountRepository.getActiveAccount()
                .map(accountModel -> encryptMessage(accountModel, messageModel))
                .flatMap(messageTO -> webSocketService.sendMessage(convertToJson(messageTO), getContactKey(messageModel).getKeyBase64()));
    }

    private ContactKeyModel getContactKey(MessageModel messageModel) {
        return messageModel.getConversation().getContact().getContactKey();
    }

    private MessageTO encryptMessage(AccountModel accountModel, MessageModel messageModel) throws UnsupportedEncodingException {
        MessageTO messageTO = encryptionService.encryptMessage(getContactKey(messageModel), messageModel);
        messageTO.setFrom(accountModel.getKeyBase64());
        return messageTO;
    }

    private String convertToJson(MessageTO frame) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(frame);
    }
}
