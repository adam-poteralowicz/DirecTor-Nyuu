package com.apap.director.client.domain.interactor.message;

import com.apap.director.client.data.net.service.WebSocketService;
import com.apap.director.client.domain.interactor.base.BaseInteractor;
import com.apap.director.client.domain.interactor.contact.GetOneTimeKeyInteractor;
import com.apap.director.client.domain.model.MessageModel;
import com.apap.director.client.domain.repository.AccountRepository;
import com.apap.director.client.domain.util.EncryptionService;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * Created by Alicja Michniewicz
 */

public class SendMessageInteractor extends BaseInteractor<MessageModel,String> {

    private WebSocketService webSocketService;
    private EncryptionService encryptionService;
    private GetOneTimeKeyInteractor getOneTimeKeyInteractor;
    private AccountRepository accountRepository;

    @Inject
    public SendMessageInteractor(WebSocketService webSocketService, EncryptionService encryptionService, AccountRepository accountRepository) {
        this.webSocketService = webSocketService;
        this.encryptionService = encryptionService;
        this.accountRepository = accountRepository;
    }

    @Override
    protected Observable<MessageModel> buildObservable(String s) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.

        return webSocketService.sendMessage()
    }
}
